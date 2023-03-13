package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.CareSheetAnswerPsychosocialEntity;
import co.edu.ucc.motivaback.entity.PollEntity;
import co.edu.ucc.motivaback.entity.PruebasEntity2;
import co.edu.ucc.motivaback.entity.PruebasEntityAnswerPsychosocial;
import co.edu.ucc.motivaback.service.AnswerService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")

public class PruebasController {
    private final AnswerService answerService;

    public PruebasController(AnswerService answerService) {
        this.answerService = answerService;
    }

    //########################### ANSWER -- captura de datos SIN IDPOLL y asignación #################################
    @GetMapping(value = "/pruebasByTime/{idPoll}")
    public List<PruebasEntity2> alertByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        List<procesaridPollAndFechas> procesaridPollAndFechas = new ArrayList<>();

        List<PruebasEntity2> pruebasEntities = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", null);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            PruebasEntity2 pruebasEntity = doc.toObject(PruebasEntity2.class);
            pruebasEntities.add(pruebasEntity);
            pruebasEntity.setCreatedAt(doc.getCreateTime().toString());
        }
        pruebasEntities.sort(Comparator.comparing(PruebasEntity2::getCreatedAt));
        String capturandoFechas = "comienzo";
        String claveAleatoria = codigoAletario();
        for (PruebasEntity2 recorriendoPruebas : pruebasEntities) { //no es conveniente hacer forEach para este caso
            String auxFecha = recorriendoPruebas.getCreatedAt();
            if (Objects.equals(capturandoFechas, auxFecha)) {
                recorriendoPruebas.setIdPoll(claveAleatoria);
            } else {
                capturandoFechas = recorriendoPruebas.getCreatedAt();
                claveAleatoria = codigoAletario();
                recorriendoPruebas.setIdPoll(claveAleatoria);
                procesaridPollAndFechas.add(new procesaridPollAndFechas(capturandoFechas,claveAleatoria)); //para capturar en un archivo externo la info
            }

        }
        //Ultimo FOR para enviar a consola
        procesaridPollAndFechas.forEach(procesaridPollAndFechas1 -> System.out.println("procesaridPollAndFechas.add(new procesaridPollAndFechas(\""+procesaridPollAndFechas1.getCreatedAt()+"\",\""+procesaridPollAndFechas1.idPoll+"\"));"));
        return pruebasEntities;
//        return null;
    }


    //****************** ANSWER PSYCHOSOCIAL - captura de información y asignación de idPoll **********************
    @GetMapping(value = "/pruebasPsychosocial/{idPoll}")
    public List<PruebasEntityAnswerPsychosocial> pruebasPsychosocial(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        List<PruebasEntityAnswerPsychosocial> pruebasEntities = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer_psychosocial");
        Query query = documentReference.whereEqualTo("id_poll", null);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        System.out.println("El numero de psychosocial es: "+documents.size());
        for (QueryDocumentSnapshot doc : documents){
            PruebasEntityAnswerPsychosocial pruebasEntity = doc.toObject(PruebasEntityAnswerPsychosocial.class);
            pruebasEntities.add(pruebasEntity);
            pruebasEntity.setCreatedAt2(doc.getCreateTime().toString());
        }

        pruebasEntities.sort(Comparator.comparing(PruebasEntityAnswerPsychosocial::getCreatedAt2)); //Ordenamos los datos por fecha
        /** Codigo extractor */
        String fechaDespegue = "2022-11-16T21:25:45.430913Z"; //COMIENZO -- puede ser falso, pero mejor verdadero para que no altere los contadores
        Instant capturandoFechasInstant = Instant.parse(fechaDespegue); //COMIENZO
//        String claveAleatoria = codigoAletario();
        long diferenciaEntreFechas = 0;
        int contador=-1;//-1 ya que el sistema empieza con un dato falso para que la condicón se cumpla y despegue en 0
        ListasPsychosocial listasPsychosocial = new ListasPsychosocial(); //La utilizaremos un poco mas abajo
        for (PruebasEntityAnswerPsychosocial recorriendoPruebas : pruebasEntities) { //no es conveniente hacer forEach para este caso

            Instant auxFecha = Instant.parse(recorriendoPruebas.getCreatedAt2());

            /** condición en base a los milisegundos de envío de información */
            diferenciaEntreFechas = ChronoUnit.MICROS.between(capturandoFechasInstant, auxFecha);
            if(diferenciaEntreFechas < 10000000 ){ //10 segundos. SI esto se cumple es porque pertenece al mismo cuestionario
                try {
                    recorriendoPruebas.setId_poll(listasPsychosocial.listaPollParaPsychosocial().get(contador));
                }catch (Exception e){

                }

            }else{
                capturandoFechasInstant = auxFecha;
                System.out.println("Las fechas son: "+ capturandoFechasInstant);
                contador=contador+1;
                try {
                    recorriendoPruebas.setId_poll(listasPsychosocial.listaPollParaPsychosocial().get(contador));
                }catch (Exception e){

                }
            }

        }

        return pruebasEntities;
    }

    // *********** ENVIO de info a ANSWER PSYCHOSOCIAL -- solo prueba ***********
    @PostMapping(value = "/care-sheet-answer-psychosocial-create-limpieza")
    public void saveComment(@RequestBody List<PruebasEntityAnswerPsychosocial> careSheetAnswerPsychosocialEntity) {
        Firestore db = FirestoreClient.getFirestore();

        for (PruebasEntityAnswerPsychosocial doc: careSheetAnswerPsychosocialEntity){
            db.collection("answer_psychosocial").document().set(doc);
        }
    }


    //########################### POLL -- captura de información y asignación de IDPOLL ###############################
    @GetMapping(value = "/pruebasColeccionPoll/{idPoll}")
    public List<PollEntity> pollSinIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        List<PollEntity> pruebasEntities = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereEqualTo("id_poll", null);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        System.out.println("La cantidad de POLLS es: "+documents.size());

        for (QueryDocumentSnapshot doc : documents){
            PollEntity pruebasEntity = doc.toObject(PollEntity.class);
            pruebasEntities.add(pruebasEntity);
            pruebasEntity.setCreatedAt(doc.getCreateTime().toDate());
        }

        pruebasEntities.sort(Comparator.comparing(PollEntity::getCreatedAt));//se ordena por fecha

        ListasAnswer listasAnswer = new ListasAnswer();
        int contador=0;
        for (PollEntity recorriendoNuevamente : pruebasEntities) { //no es conveniente hacer forEach para este caso
            recorriendoNuevamente.setIdPoll(listasAnswer.listaPollsParaAnswer().get(contador));
            contador++;
        }

        return pruebasEntities;
    }







    //###################   Gestión de DUPLICADOS idPoll -- Obtención Info -- ANSWER --- también para Cedulas #################
    /** Conslta todos los cuestionarios y los ordena por fecha de identificación,
     * ara luego obtener una lista de polls y revisar en excel cuales estan repetidos */
    @GetMapping(value = "/duplicadosIdPolls/{idPoll}")
    public List<PruebasEntity2> duplicados(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        //Se utiliza UNO de los dos depende de la necesidad
        List<String> listaPolls = new ArrayList<>();
        List<String> listaCedulas = new ArrayList<>();

        List<PruebasEntity2> pruebasEntities = new ArrayList<>();
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        /** CAMBIA el orden de ORDENADO para obtener duplicados de IDPOLL o duplicados de CEDULA */
        Query query = documentReference.whereEqualTo("id_question", 205).orderBy("open_answer");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            PruebasEntity2 pruebasEntity = doc.toObject(PruebasEntity2.class);
            pruebasEntities.add(pruebasEntity);
            listaPolls.add(pruebasEntity.getIdPoll());//Para revisar duplicados de POLLS    * Descomentar
//            listaCedulas.add(pruebasEntity.getOpenAnswer());//Para revisar duplicados de CEDULAS    * Descomentar
        }

        /** Esto se podría hacer arriba pero los logs de la consolan desordenan todo */
        //Recuperamos los datos
        for(int i=0;i<listaPolls.size();i++){ //Para IDPOLLs
            System.out.println("listaPolls.add(\""+listaPolls.get(i)+"\");");
        }

//        for(int i=0;i<listaCedulas.size();i++){// Para CEDULAS
//            System.out.println("listaCedulas.add(\""+listaCedulas.get(i)+"\");");
//        }
        return null;
    }


    //**********************  ELIMINAR -- DUPLICADOS IdPolls -- Answer Y Answer psychosocial ************************
    /** Para el proceso de eliminado de IdPOLL es mejor cada colección (ANSWER, ANSWER_PSYCHOSOCIAL, POLL)
     * por SEPARADO */
    @GetMapping(value = "/elimarDuplicados/{idPoll}")
    public List<PruebasEntity2> eliminacionDuplicados(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        List<PruebasEntity2> pruebasEntities = new ArrayList<>(); //original
        GestionDuplicados gestionDuplicados = new GestionDuplicados();
        //Se REPITE por mientras haya idPolls que traer dentro del array.
        for (int i=0;i<gestionDuplicados.metDuplicadosIdPolls().size();i++){

            Firestore db = FirestoreClient.getFirestore();

            CollectionReference documentReference = db.collection("answer"); //Cambiar aquí según la necesidad
            Query query = documentReference.whereEqualTo("id_poll", gestionDuplicados.metDuplicadosIdPolls().get(i)).orderBy("id_question");
            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            int intervalos=0; //Es la base de eliminación, es decir si de adulto hay 3 cuestionarios repetidos, es decir
            //...171 documento, entonteces esto quiere decir que la base de eliminación es 3 por lo que 1 se conserva y
            // ... dos se borran.
            int cantidadDocumentos=documents.size();
            if(documents.size()%57==0){//para ADULTOS
                intervalos=cantidadDocumentos/57;
            }else if (documents.size()%28 == 0) {//para NIÑOS
                intervalos=cantidadDocumentos/28;
            }else{
                intervalos=1;//para que el modulador de 0 y no entre al Eliminado
                System.out.println("Revisar este dato");
            }

            int contador=0;
            System.out.println("La cantidad de intervalos para este caso es: "+intervalos);
            for (QueryDocumentSnapshot doc : documents){
                contador=contador+1;
                PruebasEntity2 pruebasEntity = doc.toObject(PruebasEntity2.class);
                pruebasEntities.add(pruebasEntity);
                pruebasEntity.setCreatedAt(doc.getCreateTime().toString());

                System.out.println("El id de este documento es: "+doc.getId());
                if(contador%intervalos!=0){
                    System.out.println("Este hay que eliminar: "+doc.getId());
                    db.collection("answer").document(doc.getId()).delete(); /** LINEA QUE ELIMINA */
                }
            }
        }//FIN FOR hechizo

        return null;
    }



    //###################   ELIMINAR -- duplicados CEDULAS  #################
    /** Elimina los dotos duplicados de CEDULAS de las colecciones ANSWER, ANSWER_PSYCHOSOCIAL, POLL simultánemente*/
    @GetMapping(value = "/eliminarDuplicadosCedulas/{idPoll}")
    public List<PruebasEntity2> duplicadosCedulas(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        GestionDuplicados gestionDuplicados = new GestionDuplicados();//se llama la clase de abajo y luego su metodo

        for (int i=0;i<gestionDuplicados.metDuplicadosCedulas().size();i++){

            Firestore db = FirestoreClient.getFirestore();
            CollectionReference documentReference = db.collection("answer");
            Query query = documentReference.whereEqualTo("id_question", 205).whereEqualTo("open_answer", gestionDuplicados.metDuplicadosCedulas().get(i));
            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            int contadorDePares=0;
            List<String> listaProcesarFicha = new ArrayList<>();
            List<String> idNoencontradosFichaElim = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents){
                contadorDePares=contadorDePares+1;
                PruebasEntity2 pruebasEntity = doc.toObject(PruebasEntity2.class);
                /** Empieza proceso */
                System.out.println("Los idPoll para esta CEDULA son: "+pruebasEntity.getIdPoll());
                //*** Empiza consulta a FICHA DE ATENCIÓN ***
                if (pruebasEntity.getIdPoll() != null) { //ya que los nulos se tratarán en otro apartado
                    future = db.collection("answer_psychosocial").whereEqualTo("id_poll", pruebasEntity.getIdPoll()).get();
                    List<QueryDocumentSnapshot> documents2 = future.get().getDocuments();
                    if (documents2.isEmpty()==true){
                        System.out.println("Ficha: NO existen datos");
                        idNoencontradosFichaElim.add(pruebasEntity.getIdPoll());
                    }else{
                        System.out.println("Ficha: SI existe");
                        listaProcesarFicha.add(pruebasEntity.getIdPoll());
                    }

                    if (contadorDePares == documents.size()) {// y FINALIZA de recorrer los N registros pertenecientes a la misma cedula
                        System.out.println("Los que si están en ficha son: "+listaProcesarFicha.size());

                        /** ### OJO: Empieza proceso de ELIMINADO ### */

                        if (listaProcesarFicha.size() >= 2) { //Eliminación CASO 1 --- En ficha de atención existe mas de dos cedulas y cuestionarios repetidos
                            System.out.println("ELIMINAR los primeros ID menos uno -1 para ficha; y ademñas; TODO de lista elim; y además; en la tabla POLL");
                            for (int j = 0; j < listaProcesarFicha.size()-1; j++) {
                                eliminarAnswerByIdPoll(listaProcesarFicha.get(j));
                                eliminarAnswerPsychosocialByIdPoll(listaProcesarFicha.get(j));
                                eliminarPollByIdPoll(listaProcesarFicha.get(j));
                            }
                            for (int j = 0; j < idNoencontradosFichaElim.size(); j++) {
                                eliminarAnswerByIdPoll(listaProcesarFicha.get(j));
                                //No es necesario Psychosocial, porque se supone que no está
                            }
                        }else
                        if (idNoencontradosFichaElim.size() >= 1 && listaProcesarFicha.size()==1) { //Eliminación CASO 2 -- Solo hay un registro en FICHA de atención y los demás solo están en INSTRUMENTO
                            System.out.println("ELIMINAR TODO lo de esta lista Elim: TODO, también los del POLL");
                            for (int j = 0; j < idNoencontradosFichaElim.size(); j++) {
                                eliminarAnswerByIdPoll(idNoencontradosFichaElim.get(j));
                                eliminarPollByIdPoll(idNoencontradosFichaElim.get(j));
                                //No es necesario Psychosocial, porque se supone que no está
                            }
                        }
                        else {
                            System.out.println("GUARDAR esta info");
                        }

                        /** Finaliza eliminado */
                    }
                }

            }
        }
        return null;
    }

    //##### MÉTODOS para ELIMINACIÓN por idPoll  ANSWER -- ANSWER PSYCHOSOCIAL ########
    public void eliminarAnswerByIdPoll(String idPoll) throws ExecutionException, InterruptedException {
        System.out.println("Hemos entrado al proceso de ELIMINADO ANSWER para: "+idPoll);
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            db.collection("answer").document(doc.getId()).delete();
        }
    }
                        //PSYCHOSOCIAL
    public void eliminarAnswerPsychosocialByIdPoll(String idPoll) throws ExecutionException, InterruptedException {
        System.out.println("Hemos entrado al proceso de ELIMINADO ANSWER PSY para: "+idPoll);
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer_psychosocial");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            db.collection("answer_psychosocial").document(doc.getId()).delete();
        }
    }

    public void eliminarPollByIdPoll(String idPoll) throws ExecutionException, InterruptedException {
        System.out.println("Hemos entrado al proceso de ELIMINADO POLL para: "+idPoll);
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            db.collection("poll").document(doc.getId()).delete();
        }
    }

    // ############ ELIMADO SIMPLE ###################3
    @DeleteMapping(value = "/eliminarDocumentoSimple/{idDocumento}")
    public void eliminarDocumentoSimple(@PathVariable String idDocumento){
        Firestore db = FirestoreClient.getFirestore();
        db.collection("answer").document(idDocumento).delete();
    }

    //////// ### fin metodos eliminacion ### //////////







    //################### CONTADOR DUCUMENTOS #################
    @GetMapping(value = "/contadorDocuemntos/{idPoll}")
    public List<PruebasEntity2> contadorDocumentos(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        List<String> listaPolls = new ArrayList<>();
        listaPolls.add("GmGpBehbLq");
        listaPolls.add("PISLX-4eOu");
        listaPolls.add("P_SdUamWVv");
        listaPolls.add("S7-eJBZK3F");
        listaPolls.add("Slb_J5fVG0");
        listaPolls.add("cZiKMUKJjT");
        listaPolls.add("fTB9E_x0Hc");
        listaPolls.add("vrN1n894-n");
        listaPolls.add("wHaf-WST-e");

        for (int i=0;i<listaPolls.size();i++){
            List<PruebasEntity2> pruebasEntities = new ArrayList<>();
            Firestore db = FirestoreClient.getFirestore();
            CollectionReference documentReference = db.collection("answer");
            Query query = documentReference.whereEqualTo("id_poll", listaPolls.get(i));
            ApiFuture<QuerySnapshot> future = query.get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            System.out.println("La cantidad de documentos es: "+documents.size());
        }

//        return pruebasEntities;
        return null;

    }

    //#### obtener idDocumento simple Firebase
    @GetMapping(value = "/consultaDocuemntoSimple/{idPoll}")
    public String obtenerIdDocumento() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        DocumentReference docRef = db.collection("answer").document("d0bY5EMcIe2fdysPFY3g");
        // asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
        // ...
        // future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        } else {
            System.out.println("No such document!");
        }

        return "hola";
    }

    //PRUEBAS CON FECHAS
    @GetMapping(value = "/pruebasFechas/{idPoll}")
    public List<PruebasEntity2> probandoFechas(@PathVariable String idPoll) throws ExecutionException, InterruptedException {

        String fechaModelo = "2022-11-17T15:54:20.646478000Z";
        String fechaModelo2 = "2022-11-17T15:54:50.647471000Z";

        Instant fechaParseada1 = Instant.parse(fechaModelo); //Instant maneja el formato en que se guarda en Firebase
        Instant fechaParseada2 = Instant.parse(fechaModelo2);

        Instant resultadoResta = fechaParseada2;
        System.out.println("La resta de las fechas es: " + ChronoUnit.MICROS.between(fechaParseada1, fechaParseada2));
        return null;
    }

    public String codigoAletario(){
        Random random = new Random();
        String generatedString = random.ints(35, 91)/** Limites en código ASCII */
                .limit(10) //maximo de dígitos
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public void empezarACargarElArray(){
        List<procesaridPollAndFechas> procesaridPollAndFechas = new ArrayList<>(); //Ahora solo sirve para la tabla POLL
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-17T15:48:25.460300000Z",">,U80A6;,S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-17T21:19:13.320328000Z","?=C72EJQ2%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-20T15:44:59.355366000Z","%=K+S?O16X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-21T20:19:49.639920000Z","<X71=BWSXD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-22T20:47:35.845096000Z","B);C08-$@R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T04:48:20.446721000Z","H1:GC*E$(B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T17:04:47.299139000Z","6IN3Y8NPB3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T17:44:13.142856000Z",")3EZUKVK3A"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T19:13:36.658848000Z","4>LZ0PX.Z0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T22:20:25.597154000Z","0/R@W4'B/)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T23:19:12.464826000Z","/N5:C:$8$N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T23:21:22.413365000Z","MGAC2N#QJ<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-23T23:43:40.490006000Z","'<L)2,JKZ7"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-24T01:33:21.781482000Z","RV1&M'8$5F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-25T23:59:29.421597000Z","*RE4Z3=IE-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-26T15:35:47.458540000Z","@K92<:JMYN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-26T16:12:50.366098000Z","C8V<Q03K%N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-26T16:55:48.150628000Z","/KHC6D=))F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-27T21:59:09.116068000Z","NZ,3<C>COI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-27T22:10:55.703685000Z",")L7=D%#I3T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-27T22:25:54.654847000Z","B6@Z*G:4LR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T00:33:38.727148000Z","%73(F9GS4T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T00:49:11.467653000Z","9F:-%PZ/@#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T03:41:40.392716000Z","R#*6$N%VK8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T14:13:49.844785000Z",".N5EF17Y#9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T15:40:08.081754000Z","L:JLE-;&@J"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T20:01:08.716208000Z",">)'0*0PS'4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T20:14:07.624044000Z",">0CYVS=6#E"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T20:28:32.527726000Z","3R>3L9OZ;;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T21:24:05.360899000Z","SR'M4O1?',"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T22:23:54.341707000Z","9,Z-:OI#XN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-28T23:27:55.218459000Z","#QC,LD0-I="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T02:25:29.449672000Z","+>X#UK?,3Y"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T02:36:22.624581000Z","/8(U2.H&$<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T02:46:53.435019000Z","52W90NXK;H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T02:55:13.935927000Z","HMOL(9R9$#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T02:59:52.336533000Z","IPBBJ?P/IO"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T03:15:11.503035000Z","VX694Q/MU1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T03:59:32.489451000Z","-#TLL)$6(@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T04:10:41.704618000Z","IF?;8=ETNI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T04:19:16.631503000Z","W?'WW+9OD."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T12:57:02.474798000Z","%3,'?//5IW"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T13:41:12.207696000Z","MYTZI&ZT<W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T13:46:00.834169000Z","NZXC%VV30T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T14:25:02.223812000Z","GYF)UMK;:K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T14:44:37.838625000Z","(<L;E&#16S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T14:47:32.845050000Z","#K,2'9-EZ,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:00:24.985959000Z","?F%5BSW#+'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:02:44.121346000Z","0P&$.8YQ<F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:07:43.097165000Z","Z'TB$/4,:D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:08:33.278288000Z","9RAQFD3>TH"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:18:25.666729000Z","+12S?X(29L"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:19:24.629078000Z","R4<TQYFE8T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:22:09.261546000Z","ED'U('Q:EK"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:23:09.914434000Z","O+&BF*XBT6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:30:22.968829000Z","0E<7T:=IOM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:31:43.809555000Z","13>/+*WX8C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:43:49.242402000Z",">JP*>C/?S@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:50:39.536780000Z","AZ.V)8'OIL"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:53:26.564792000Z","@HW5M4<J0E"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:53:31.120731000Z","9V;N'GHQC>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T15:59:04.969570000Z","U;E9(<$Q*="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:21:26.556582000Z","Y96BK=XVB+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:31:03.771091000Z","%NAF9E1+H<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:37:08.767695000Z","WOXY%FC0X'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:37:25.778461000Z","@PTU?<P5Z>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:49:05.272839000Z","ENHO+9Q>;C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T16:58:08.436380000Z","C4@=W(-@VQ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T17:28:49.336756000Z","M7)4K<Z5QJ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T17:32:53.615932000Z","+S,AB@?9O/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T17:34:46.211196000Z","T$?F2PF7&F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T17:52:03.542215000Z","7-+E55;IB7"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T18:20:07.181479000Z","ZP;UM,,@SZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T19:02:06.566878000Z","-)&$8/0@L:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T19:10:26.438333000Z","=M)(;(:<'/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T19:17:07.139992000Z","?;?BFI&>;Q"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T20:23:56.844247000Z","S#FV7BD.DC"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T20:34:14.306612000Z","57NAEW,ZX-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T20:37:56.553692000Z","&8I++@UXZI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T20:44:05.123180000Z","GNM@82C60F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T20:54:06.197361000Z","@<M*8/@;CM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T21:06:51.787456000Z","TU7<74@#88"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T21:15:25.478397000Z","#:,<GLYW%1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T22:29:06.908816000Z","T0R/NQ196="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T22:36:59.556427000Z","0'OHG((H#)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T22:52:07.545147000Z","*3?4)81CSV"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T22:59:31.908349000Z","M#B8II8&-M"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T23:21:12.001324000Z","4)SCZ*'RX1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-29T23:48:47.592472000Z","++'=%3OP%W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T00:13:28.623427000Z","@1LQUD%),V"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T00:28:11.816722000Z","+KKZGP'IEK"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T00:42:05.683448000Z","Y#/Q'3L$#W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T01:27:44.434273000Z",",EMUZ26@$T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T01:39:22.187921000Z",";$NHXZ)37>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T01:49:38.190927000Z","$0#PKR.U6L"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T01:51:51.823055000Z",":NJA'9EVCR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T01:59:34.367011000Z","5$(JTJ$I0M"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:02:16.368957000Z","68<*75OI'$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:05:51.260228000Z","J8?F(00XI)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:12:08.371681000Z","2X(Z@<2$9C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:17:00.830647000Z","X%E=SK*?*I"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:33:09.447275000Z","A@N2=4ZUFO"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:43:04.092845000Z","Y.I%-,IRRF"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:50:37.414616000Z","YMYD8K0P5+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:53:06.666636000Z","CD'&K6QR7("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:53:26.507577000Z","G>3(9&%DU-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T02:56:57.754794000Z","-FK9R?-?@M"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T03:03:21.798269000Z","M$V01&$?F."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T03:13:08.650249000Z","L$C$,I#@1#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T03:22:02.254073000Z","$QD:&DX@L&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T03:30:44.271984000Z","R7W<NE6OR-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T03:41:37.760943000Z","@&G6K5+@#D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:07:40.671146000Z","UE'6F'+UJ6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:19:49.449110000Z","BS.,#O&3YY"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:27:54.882211000Z","?-LY.A/D;K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:27:57.298406000Z","G(NP.,W?0,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:34:08.386312000Z","H.1+X%N<QW"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:39:03.929018000Z","83$N<GJG/A"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:41:05.112734000Z","Z)BDEZWL#4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T14:43:43.406809000Z","THD4I&E&29"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:01:19.335057000Z","5LY=CTJ-=H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:03:43.006818000Z","Q=#9Q6S;.>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:09:31.221919000Z","CCBZ&K.Q%;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:12:09.462914000Z","L9M7SLC>HY"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:17:25.275550000Z","TMM#2G@916"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:21:49.876812000Z","INYU4$/X9<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:24:40.800664000Z","+K'G=<V,0D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:27:12.946370000Z",".MC4)$@*02"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:30:33.073036000Z","9BMEN@RLD4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:32:37.704050000Z","LGXI'1=U.'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:35:10.456330000Z","IC3#&/R,<("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:39:13.881831000Z","UMP7#B)4HW"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:44:44.583361000Z","FNS.%/,EF4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:50:10.832369000Z",".4$1/ZI3BQ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:50:13.922683000Z","R+<.0UV6&W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T15:53:23.470319000Z","NV#C9LJI+/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:03:16.869125000Z","-M;8:.K&#C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:03:24.641944000Z","(GQ=ZD)DO*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:06:30.197477000Z","W95<$>8UZ3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:12:03.767916000Z",",;H)KSS$IN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:16:47.042083000Z","ISAZXLPTH;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:20:27.664927000Z","IC/%EC))K'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:22:14.275812000Z","*T,#FN0-$#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:35:30.996589000Z","0X7Q9LXA#P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:37:23.008549000Z","'):0NFWGBZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:37:51.998696000Z","/#AK9Y,BWA"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:37:56.320805000Z","LA9B?X)A:W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:40:03.026208000Z","UZ2&X(7;O3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:40:51.699458000Z","0E(VA/=6($"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T16:52:57.744742000Z","R6$,2/F>M<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T17:25:17.033378000Z","6?'GBY$(2O"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T17:54:53.034635000Z","'S<9'IL$:'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T18:15:24.599699000Z","0MUJWX9D/3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T18:15:33.102007000Z","7Q36S1I&?."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T18:42:10.043034000Z","IZZMO5O'GN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T19:09:29.122801000Z","<R/-Y.OVKB"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T19:35:42.742448000Z","DE+4AIK8BS"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T20:41:49.447710000Z","R*TY/:TX38"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T21:02:18.867512000Z","*.*SC?-N.T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T21:33:34.178168000Z","*D1,Z*F?N+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-11-30T23:51:34.650806000Z",":I@L8B-NR1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T00:01:19.329096000Z","VO@?K*0FL*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T00:10:19.910203000Z","U>>YW'85ZR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T00:18:27.771454000Z","KF<M4$R0+>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T00:26:46.430432000Z","/1(?YY@$I?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T00:50:10.001577000Z","+WLG'V1L3S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:11:45.399851000Z","KLAMFV&MAF"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:24:34.557582000Z","<OB+9LN9'Q"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:42:44.803492000Z","DMUUV/$&>W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:48:22.019144000Z","TOG$CYMC,$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:50:53.482763000Z","MU*E09TNP/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T01:51:52.665201000Z","-QGP3=IPRM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:09:29.358887000Z","XF5MF?S&,2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:17:59.335005000Z","R#,9JG35*;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:18:19.127599000Z","XV*AC=ATW#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:26:54.087737000Z","WT:KAZ;45#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:38:28.461003000Z",")DG-#784$C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:54:33.368241000Z","0TSZ,FI''8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T02:56:32.901963000Z","%Z.Q</%QW/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:04:50.181472000Z","-)LG,G5NI6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:10:01.035026000Z","B6D#N7@(R9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:15:10.711846000Z","4%HDNV?G'0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:27:15.435134000Z","69V?/8I7)#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:31:27.987154000Z",".D3://Y$%/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:31:49.428952000Z","R*IE5/O%U1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T03:32:00.083844000Z",".T9K-COPR7"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T14:44:40.887783000Z","XZ&(@'&0YV"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T15:44:28.469094000Z","E/41OR>6YR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T16:54:59.228588000Z","#.'.D@#HOX"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T20:55:10.230714000Z","W=T%CHJ8%B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-01T22:30:46.681623000Z",":D&74FCVEE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T01:01:07.097505000Z","0'+E(VY2=0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T01:55:26.560328000Z","*3L.7,3G#>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T13:16:37.826909000Z",")+V*TF<DK&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T13:34:23.292476000Z",".FU/%(?$BA"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T14:58:22.568131000Z","<42Z*JU0M<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T16:47:16.355072000Z",">NK(W.Y?4Y"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-02T17:11:52.324416000Z","3BV$N'X4CE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T00:19:27.288292000Z",";1<#N5B$J/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T00:47:02.586093000Z","#QGW;UAH.*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T01:42:33.815648000Z","<6ZKJ1?@1B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T02:22:23.881468000Z","BLDJKQ@S'E"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T02:29:46.358732000Z","TYC'G68)KG"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T02:35:49.282375000Z","L*GJRI*10$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T03:09:39.491293000Z","/HF/QTW467"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T03:13:44.676842000Z",".)FYE'GH&2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T03:17:51.455980000Z","25+9S./,XO"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T03:21:38.986367000Z","VAK?#@-Z:*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T14:33:43.642661000Z","6N=UE90WBO"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T15:11:29.375824000Z","OV.C*79:9S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T15:32:00.994270000Z","4S*&>ZDU8("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T15:41:20.331817000Z","5@8B(974--"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T16:45:26.260328000Z",":T7*//R&FG"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-03T19:37:51.582180000Z","*+>9#F$U-F"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T01:03:02.728216000Z","V1C4GG'Q03"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T01:27:39.226102000Z",">4?>C0=<$0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T01:53:26.791527000Z","-YJ.Z9#ZW>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T13:18:06.742191000Z",">7?TV@D/9W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T14:07:09.502734000Z","AR(07-7JUK"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:01:19.063957000Z","A<.5>Z(;9-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:07:45.253929000Z","Z#O&;TO%A#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:15:24.901240000Z","/I$68X$PK?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:44:49.420863000Z","C/<VM:$0<R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:58:49.008390000Z","*7?PY$B4?J"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T16:59:58.148145000Z","*/WTZ.F/D#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T17:49:36.214238000Z","'CY@LS(+1>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T18:22:33.556407000Z","J=<#85UMF-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T20:36:01.474384000Z","4S-:?9NB08"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T21:20:21.520790000Z","Y#L;GCHYIL"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T21:36:39.721903000Z","I7+8?PG%XZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T21:49:15.143431000Z","B/5@,'5>EZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-04T22:42:01.641350000Z","#44%C(:4S="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T01:13:43.893222000Z","LC?4>(P<(K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T13:42:37.475445000Z","7*(4*5Z&B*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T14:18:52.118799000Z","IQNF)/%+*#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T14:19:49.090244000Z","@D.%9FV-+A"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T14:52:05.208422000Z","K.U%5GO(#H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T14:52:25.571103000Z","JN++'U.HL("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T14:52:48.520818000Z","*(#.L5?#U;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T15:02:59.321748000Z",";HY9ZHS)8H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T15:18:26.775036000Z","7-PO%=<20/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T15:23:10.755814000Z","*H/8K'I52B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T15:43:48.297282000Z","Z$T+%STL94"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T16:28:55.369975000Z","@AZAIJ46)Y"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T17:43:38.537925000Z","2%?509$:?."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T17:44:54.820039000Z","Z64BX5CU8K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T17:45:47.514703000Z","7;2&V)P7>Q"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T17:56:06.257182000Z","G)H5.5CXAY"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T18:10:15.609771000Z","GK&'R4?;#1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T18:11:03.273383000Z","S9X8->)1B2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T18:25:51.172047000Z","#<L(C2:JRR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T18:26:35.174153000Z","W9DOP-E<LD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T19:05:10.544776000Z","EJ%T/F%+-H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T19:25:23.214879000Z","J%3OZ96FR)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T20:44:22.019435000Z","O&<-)/O/69"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T22:02:32.862109000Z","'8FO+(RSMW"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:04:34.567527000Z","Q/-?#)SQNE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:14:59.452670000Z","J,8$.(XBV1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:16:10.883237000Z","L'IIV9>@-N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:16:52.457397000Z","/:Q-18#7N;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:20:53.727373000Z","FE4RL'*N.)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-05T23:50:18.894524000Z","87&PYC5U;W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:23:59.704155000Z","%1.DAJP:$P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:34:59.912115000Z","6=F=9M.7LU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:39:40.371726000Z","?JCQ-+<R8>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:41:06.405781000Z","F4&*>=5L04"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:42:27.835369000Z","=$A,2A8IE4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T00:44:43.719939000Z","?7:HM<H.<?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T01:19:21.915424000Z","I;TL?DK8+9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T01:38:45.950392000Z","P;U7574B/4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T01:46:36.133225000Z","YI&(QNI6$U"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T01:55:04.530643000Z","0W?D@YNZGI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:11:26.967034000Z","EA03J=86O6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:19:36.984857000Z","5T@)TC?LFE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:22:00.928310000Z","A'X<K#>RKM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:22:12.241747000Z","O?HI4FACU%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:25:56.754796000Z","*VCPX'7%3I"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:30:06.449168000Z","C-$/X319@E"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:35:13.366112000Z","-IHO86+9EP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:36:51.684022000Z","TDPIZ.I>S."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:40:10.523462000Z","Y'592+B?<?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:40:18.634206000Z","L7..*X52RQ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:50:27.724612000Z","K<M-O4(N.)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:52:03.324330000Z","5E1I8&<-6G"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T02:54:47.113111000Z","#@Q*+6FTIZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T03:01:45.448003000Z","+D0S9FT/'W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T03:11:08.029542000Z","3%FJAOJR/D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T03:13:30.366760000Z","=+XH8+D60E"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T03:30:22.439632000Z",")C5B/+4HR$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T12:58:36.979586000Z","JZ)MOSN=-<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T12:59:26.829606000Z","&%5=S$H<-V"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T13:00:16.109530000Z","IW.'K$E<7?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T13:17:55.127021000Z","N;#)I&G7C$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T13:17:57.332610000Z","3X4$I,U.Y&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T13:35:01.241640000Z","4.4@<RV+V>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T14:07:13.192364000Z","-'.MN/7@=D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T14:26:15.272581000Z",".0.RH@ML&X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T14:54:38.710216000Z","(>TACAB;WH"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T15:08:42.338700000Z","6,E#)>VKMF"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T15:09:20.375956000Z","*C%BAUCZEX"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T15:38:09.253582000Z","++CMS9=#6."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T16:22:34.509560000Z","Y7:K4',/RL"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T16:24:22.664590000Z","-=7<LO/*I8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T16:54:53.462041000Z","/JICJIUCXG"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T19:21:13.446104000Z","S?8,WJV'*V"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T19:55:10.646284000Z","BFG<W@D5;B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T20:14:05.704210000Z","5*JAQ./W>6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T20:40:48.483920000Z","O($-KOOURE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T20:41:57.851445000Z","V@Y@B$%SPI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T20:55:08.180719000Z","<BBM@4S*4@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T21:05:12.297053000Z","FF1QMT<>T+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T21:23:27.759409000Z","(AYCK,UTB?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T21:35:41.551788000Z",":YO;IN6JQN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T21:37:39.430385000Z",".SZSI<4I.:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T22:34:51.357897000Z","OP-F35V,.+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T22:47:27.727135000Z","11?<=6&VAI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T23:11:19.564199000Z","P/78O(X8S1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T23:25:13.516759000Z","J'8*>Q>8)-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-06T23:56:01.638565000Z","9?.KYP&;*="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T00:08:20.757071000Z",">X.%++O)A&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T00:23:20.490769000Z","YF24@FWQZ/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T00:35:38.716844000Z","#74'XN*:NU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T00:55:37.042162000Z","ULYMJ69U-@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:11:35.756325000Z","T4X@;U1%>M"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:14:20.586976000Z","G3VC&)&ZQZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:23:15.422473000Z","1?LR<#P1XJ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:24:09.197574000Z","O9@BQ8FMZK"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:25:41.794590000Z","E0YYEH)(TC"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:38:09.556317000Z",")NY#/;75+7"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:39:15.096986000Z","C@OHG2>X0$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:41:28.457536000Z","5OI?66.06&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:41:43.022015000Z","NQ@WWUKW0Y"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:51:02.302583000Z","TC6S55KX0-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T01:51:28.543006000Z","7=#/(A@-BM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:00:18.311860000Z","$6EG;5>ZLX"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:02:32.015361000Z",",VPQGYO0'@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:16:55.506721000Z","P@VS2=V,%."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:19:24.523097000Z","7>/FZV@E)>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:19:55.092883000Z","OPAY=P?G>K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:24:00.643211000Z","Q5UDY''G9R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:36:53.594224000Z","Q6OK%D//6;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:37:20.771516000Z","?0?1C@FQ*#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T02:43:35.772553000Z","7V*$)ED#<="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:01:01.020712000Z","EJSR#R;,EG"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:03:49.417889000Z","$AIE8Y7IV&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:04:19.273556000Z","C$QC&N.S3-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:04:47.637813000Z","X@#A@,I5GC"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:04:52.365326000Z","AB2P9L3$TD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:12:31.738713000Z","N>XY&%J)Q%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:12:51.077200000Z","9K(-7;MIUL"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:22:22.646337000Z","@SFT.Z-.L("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:23:35.630735000Z","6GHO/*26J)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:36:53.207552000Z","QWQ3?8ID6K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:43:25.422043000Z","3NO)V1(>U("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T03:57:27.175748000Z","CM,)IJMPHF"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T04:23:26.579651000Z","M;*:U'M#2G"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T04:35:06.038535000Z","3N4&(/.4SV"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T04:45:12.309571000Z","'*+I7P&V)'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T05:09:22.808807000Z","D=2=S=1I-D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T05:37:10.803155000Z","7*=FCAFRP0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T05:56:15.349620000Z","DY.4N.3=#*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T11:20:12.517132000Z","(/IF#B)HL2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T15:19:09.362078000Z","@L3HRL;JZ2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T16:26:39.558323000Z","S)SMBGV0SS"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T16:35:26.598674000Z","%=PNOEFA0("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T16:46:23.694305000Z","2+%PFCG;V5"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T17:08:24.330301000Z","9/PV@FA#QU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T17:20:56.456094000Z",":D(7*M36KB"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T17:32:27.442855000Z",";2$Y.3;OR:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T17:35:55.557912000Z","WU&CW@O=WP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T18:08:04.367912000Z","G@:+=&?4>4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T18:22:04.196002000Z","@-N*V&CVMJ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T20:00:58.638942000Z",".=?8A<T/2H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T20:24:04.662789000Z","CI5'%5?*%$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T21:27:45.505192000Z","1:IWS'J:HR"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T22:27:45.629361000Z","4B@T2LZ>54"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T22:44:49.775903000Z","'V<J&JTU+R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T23:00:11.805025000Z","(HQJTUZL+Z"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T23:12:25.308352000Z","%&IB,W7O+C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-07T23:16:02.204290000Z","X2SOE@U>BS"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T02:18:45.858386000Z","NQKW%@K2%,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T02:33:52.604096000Z","9S$HV:(W3?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T02:42:51.896276000Z","+N/B#>5Z-1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T03:00:39.809896000Z","J+P-WC>B%P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T03:25:55.430288000Z","G4S1F5@>OH"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T03:41:25.697607000Z","PM/R&9'G4T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T16:54:40.495430000Z","-5WON$P:3>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T17:31:31.571550000Z","WR/ZV9*0@Z"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T17:57:21.300037000Z","YJ.7OT*-XD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T18:56:04.472183000Z","OJCV*AJ+<0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T19:07:27.672009000Z","81+.D?<;;9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T19:17:16.632794000Z",";G#?X96VL@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T19:26:12.637792000Z","2-CY:3+19;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T19:30:14.884250000Z","5W;,AH?#SO"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T19:47:26.941927000Z","01W)IS*3$>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:02:32.361640000Z","BU2YM938K,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:11:15.345956000Z","T$0XRP&D+'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:14:02.194457000Z","NFX2EHMSXQ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:23:20.805807000Z","P9(>Q?.3UY"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:26:51.430629000Z","2*NWM,%,KM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:31:54.869133000Z","LJ*7VFMEB&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T20:51:04.810598000Z","-N0X,FKMWC"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T21:05:47.768519000Z","R39UZGDXF<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T21:15:59.895738000Z","5WUQBGPSI."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T22:24:01.848838000Z","O.VK46TR7H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T22:35:44.743397000Z","#XN2O-$5VM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-08T23:02:42.538804000Z","&;&T(AS&-0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T15:13:23.690534000Z","ACA?+8BO'*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T18:26:19.457363000Z","9@K+S&>$6Z"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T18:37:32.659220000Z","E$-7M&J78?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T19:40:50.884225000Z",":N)0@J9X/1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T19:48:17.904669000Z","P-Z%O2;SM-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T19:53:54.404233000Z","54.M0&0T3Q"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T20:03:00.717513000Z","HUDH(/K8C'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T20:24:42.430523000Z","F3)2;WV)J."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T20:29:04.987656000Z","0X1?2&E(RU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T22:00:02.911081000Z","O7B<MWGW1("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-09T22:14:41.505184000Z","$S*KIA3OL<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T00:24:30.136741000Z","%1BHI*9I/)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T00:41:27.000223000Z","GI$28/?5#2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T00:56:25.173606000Z","OPD'VXF##-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T01:34:35.871143000Z","AA-Y,>#PJC"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T03:11:36.186162000Z","=OG4@/=DZ1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T03:28:31.965225000Z","E?<3WY=I24"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T03:39:19.031503000Z","6DIKEYF2I="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T03:50:03.409927000Z","F%8T,$%VO%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T15:13:01.907171000Z",":K1G*(H*4X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T16:42:38.324143000Z","UPUXUY/LLT"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T21:36:44.107150000Z","VE=?XF$XDN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T21:36:46.195495000Z","&=&Q(3Z:B'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T22:34:17.256354000Z",":-M-6?LTS."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-10T23:26:36.664059000Z","K'*)@;I959"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T00:41:30.235437000Z","+7.9L;<T'6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T01:26:25.634072000Z","Z2,B93:XB&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T01:44:02.466912000Z","<Q?R1)T3.C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T01:46:35.740005000Z","OS=.&JXXU%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T01:56:04.121047000Z","<6YUMRV;D$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T01:57:39.681011000Z",",.LLH1).;X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T02:07:24.922076000Z","0OXJJ(-0GA"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T02:18:17.122235000Z","%;B+UI+FI@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T03:02:52.614436000Z",",<E=EZJ<G("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T03:13:57.813729000Z","8S)O3V13LS"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T03:25:56.333020000Z","<UZCS9-D=P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T04:36:41.556023000Z","-DO/L$'+:I"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T05:04:25.139134000Z","T5CW0&.-)S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T14:48:59.690929000Z","=JCNNI9F1("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:04:56.712394000Z","5.&*AF.R=<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:07:16.853396000Z","BD#WL2=4ZN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:11:08.169494000Z","9)K:6HWX%O"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:22:02.452222000Z",")J,=52W?=3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:25:14.509493000Z","WL*T6,9SZ9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:31:11.494691000Z",">ZRLW=O%T8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:33:08.474184000Z","L<@LU7D1@X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:38:22.342819000Z","'.V%Q+M=Z)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T15:46:52.217197000Z","KE,06/F<J;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:05:55.103617000Z","A0'AZZY:(@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:15:39.910112000Z","=7E<%Z'A1:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:27:39.963415000Z","E8AG4;Y(KP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:37:15.668323000Z","BE6G?Z<$(6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:47:16.424836000Z","-A;?E94FF2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T16:54:08.641723000Z","58>+JZ5D=$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:08:43.743838000Z","DKKG,I,(6P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:18:59.724475000Z","4R/F7+>$'B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:39:30.921608000Z","+R4@*2(4WD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:43:25.021763000Z","=MD0/BJXY2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:43:25.465790000Z",">(R)MBK/UQ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T17:57:20.875153000Z","%Y=R+O8N#0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:01:19.459793000Z","TBG=V)'C2>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:08:22.184481000Z","KDT0?LP>7?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:18:03.345767000Z","K3DW6(&&8V"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:19:42.609360000Z","J.@C&IQ540"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:30:02.061919000Z","1%V#OJEPL#"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:38:22.401058000Z","D:2NG#.-MM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T18:50:40.600757000Z",":RYCAHA'T*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T19:01:58.198953000Z",".$7+'S2W'0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T19:13:12.850094000Z","A9-T3D0?Q)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T19:20:01.384077000Z","$MOZ9COBIM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T19:48:00.374163000Z","6=O3;6'(WI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T19:48:18.194949000Z","29LLCO/L?I"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T20:11:49.575183000Z","F/)X:WSU@N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T20:38:58.775615000Z","2SMHF*36<%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T21:07:35.812643000Z","ZBK&%,(>+B"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T21:28:51.858615000Z","%*GAU&T9D)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T21:39:38.870772000Z","66H>QQ>X/-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T21:52:59.256483000Z","Y;:Q<I'901"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:01:31.568285000Z","@T.5)U>B(&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:01:41.570700000Z","#FNKYD07X4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:15:16.447388000Z","2R9BMX;)B'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:16:29.197306000Z","*Y@@L')YO4"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:28:22.198006000Z","%-SMBHWL95"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:41:10.861023000Z","/XWXBCECA="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T22:56:11.222722000Z","67T2Q9S'*+"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:00:59.485937000Z","8/9FJ'3H?N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:08:05.582276000Z","QD9%S<I<-3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:21:38.478595000Z","4O4(BJ'O;D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:27:14.185290000Z","*6CZCE0?=&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:29:48.636895000Z","@)R1*>7S?/"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:39:07.872329000Z",")9LNF9,#7U"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:47:44.577349000Z","UV$/>(BP0O"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:48:56.906547000Z","0D#P@?QO,G"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:51:07.410896000Z","**/$-Z#4*;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-11T23:58:55.220560000Z","E-5-V:FT+X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T00:26:25.325481000Z","4,JPU;8A3C"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T00:27:47.264360000Z","O@Q-LK/M86"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T00:37:15.862209000Z","N3R:X#KGOD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T00:49:16.714357000Z","L(VXFCGSO:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T00:59:29.868683000Z","+,U<0$JS9S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T01:00:08.082793000Z","8GCO/W(VHU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T01:25:54.230620000Z","64J%@:>=P."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T01:31:13.480272000Z","B,VS.K=D%?"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T01:35:18.667117000Z","SCI2LCI<X<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T02:04:44.668796000Z","#5$Q-@5X>P"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T02:05:04.389392000Z","60JD/035YD"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T02:25:55.060302000Z","C3X2:G9;WU"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T02:56:10.615846000Z","N5D>JPPJ-:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T03:03:19.243902000Z","E@GOUFO3,)"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T03:07:32.488499000Z","9ED5BM#&(T"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T03:15:01.485611000Z","I$+I;A$,*G"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T03:33:29.666361000Z","89R<1YUFZ1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T03:53:20.299919000Z","%9KA/5A4CP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T14:57:46.255447000Z","CP29K.>R-:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T15:13:01.539839000Z","*)7?6F8DVJ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T15:38:06.306735000Z","NY%+8FSDEG"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T16:02:12.546098000Z","'#OKR2P25D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T16:27:54.943867000Z",".XDLWEQCE3"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T17:02:30.372688000Z","7YGB*,=H<&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T17:17:35.599573000Z","2/FVOJFP$R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T17:31:56.635530000Z",".1/>U*R@GP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T17:44:36.639928000Z","6=S9SBAY3@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T17:50:45.889671000Z","'80J@N'LL'"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T18:23:31.347930000Z","DGY:OD$=FV"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T19:01:01.862750000Z","9L'E:8V:,<"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T19:34:15.758886000Z","Q8P=2#MC$N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T19:40:38.491508000Z","Y4><3PN%@S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:24:33.130738000Z","PZTT$U&AB6"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:35:58.815947000Z","B%1V+4NDS;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:37:58.449746000Z","N0L-$I5=@5"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:45:37.530156000Z","BVG4,U?D+8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:51:44.546821000Z","KW6>,5,JZ,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T20:57:15.081716000Z","B2>Y??<5<W"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:09:48.630031000Z","%%+$4$WB?R"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:19:51.371099000Z","M:VIN?J),>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:21:41.985847000Z","K1C%TA+(VI"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:36:23.242260000Z","U4*,PC3/P7"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:46:08.616828000Z",":*$@ATQI:V"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T21:59:07.020168000Z","('BH.%)':O"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:13:32.022850000Z","PI&$RYZUAP"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:14:13.762444000Z","A'Z#XLAX31"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:14:36.168382000Z","<@#I&-WEQ*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:15:57.841624000Z","/K<1NBEZ96"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:16:39.556628000Z","G#)-;9Q#9X"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:25:43.421003000Z","8ZV8QC&DBE"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:26:31.923594000Z",")KL=@,WHGB"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:27:24.203089000Z","4GWGG;TPJN"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:34:43.622491000Z","00$CGB8UT:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:35:13.226387000Z","N@Q.Y;X?E>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:37:57.522138000Z","%?Z+L*52;D"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:38:20.390264000Z","QCR:7.CH#."));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:38:42.207014000Z","?XONG*)<2:"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:40:15.376012000Z",",=;N=O0PB$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:51:03.386382000Z",">$NVI;Z*1-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:51:29.223902000Z","F5=/X4Y?##"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:51:57.309902000Z","GC)Y)R4,(;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:54:12.523065000Z","#B1OHOR-TW"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:57:45.567844000Z","L)N9>NLJ00"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T22:58:14.363927000Z","UV59T*Z#X&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T23:07:02.508558000Z","?7%M>ELT,1"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T23:10:12.054399000Z",",)8EWSA4(K"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T23:18:19.480175000Z","?FI1RU#C14"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-12T23:32:16.283214000Z","DU+9L9F+=N"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T00:11:59.641592000Z","HZ33'//TD;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T01:10:52.454901000Z","@S8-SF(6X*"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T16:06:46.890884000Z","2VY2;<E&N%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T17:22:32.952696000Z","9Z0K.:Y&IH"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T17:22:36.520311000Z","W&JZ>0;F8@"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T17:55:18.429544000Z","@4N;UE%W0-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T18:30:14.405322000Z","%O#I<%=9RM"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T18:46:43.922229000Z","CF@@C)RLQ%"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T20:29:16.199582000Z","F%%--/3:#M"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T20:48:07.865484000Z","(R0YC@P4=("));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T21:12:57.197366000Z","E$@4&M+$O2"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T21:16:37.313522000Z","+1<U(:D<3;"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T21:24:42.916738000Z","7$CC:)XA1L"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T22:17:58.252856000Z","EZ-N.?.)P8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T22:19:09.867497000Z","Y),B?I>@T,"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T22:35:39.449129000Z","?<X>T/FEY0"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T22:54:02.520206000Z","@I&EYGD#'8"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T22:59:39.215132000Z","$,#G'6-U7H"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T23:08:02.285636000Z","OT;VB?XV5&"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-13T23:30:16.000642000Z","Q(PU5##3F$"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T00:19:50.534394000Z","8>GD#-K6MZ"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T00:28:30.494502000Z",">7TQX4<:5S"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T00:44:47.583289000Z","COE;EX/:L>"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T02:47:47.341497000Z","GBIH9MPOV-"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T02:57:46.480408000Z","D)=&NVEJR="));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T03:17:03.639445000Z","63?V1OF=ZT"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T03:31:56.847633000Z","X;>C.RW1M9"));
        procesaridPollAndFechas.add(new procesaridPollAndFechas("2022-12-14T03:42:51.485377000Z","LD=18QR<V;"));
    }

}

/** Clase almacenar un lista para Answer **/
class ListasAnswer {
    public List<String> listaPollsParaAnswer() {
        List<String> listaIdPollsAnswer = new ArrayList<>();
        listaIdPollsAnswer.add(	">,U80A6;,S"	);
        listaIdPollsAnswer.add(	"?=C72EJQ2%"	);
        listaIdPollsAnswer.add(	"%=K+S?O16X"	);
        listaIdPollsAnswer.add(	"<X71=BWSXD"	);
        listaIdPollsAnswer.add(	"B);C08-$@R"	);
        listaIdPollsAnswer.add(	"H1:GC*E$(B"	);
        listaIdPollsAnswer.add(	"6IN3Y8NPB3"	);
        listaIdPollsAnswer.add(	")3EZUKVK3A"	);
        listaIdPollsAnswer.add(	"4>LZ0PX.Z0"	);
        listaIdPollsAnswer.add(	"0/R@W4'B/)"	);
        listaIdPollsAnswer.add(	"/N5:C:$8$N"	);
        listaIdPollsAnswer.add(	"MGAC2N#QJ<"	);
        listaIdPollsAnswer.add(	"'<L)2,JKZ7"	);
        listaIdPollsAnswer.add(	"RV1&M'8$5F"	);
        listaIdPollsAnswer.add(	"*RE4Z3=IE-"	);
        listaIdPollsAnswer.add(	"@K92<:JMYN"	);
        listaIdPollsAnswer.add(	"C8V<Q03K%N"	);
        listaIdPollsAnswer.add(	"/KHC6D=))F"	);
        listaIdPollsAnswer.add(	"NZ,3<C>COI"	);
        listaIdPollsAnswer.add(	")L7=D%#I3T"	);
        listaIdPollsAnswer.add(	"B6@Z*G:4LR"	);
        listaIdPollsAnswer.add(	"%73(F9GS4T"	);
        listaIdPollsAnswer.add(	"9F:-%PZ/@#"	);
        listaIdPollsAnswer.add(	"R#*6$N%VK8"	);
        listaIdPollsAnswer.add(	".N5EF17Y#9"	);
        listaIdPollsAnswer.add(	"L:JLE-;&@J"	);
        listaIdPollsAnswer.add(	">)'0*0PS'4"	);
        listaIdPollsAnswer.add(	">0CYVS=6#E"	);
        listaIdPollsAnswer.add(	"3R>3L9OZ;;"	);
        listaIdPollsAnswer.add(	"SR'M4O1?',"	);
        listaIdPollsAnswer.add(	"9,Z-:OI#XN"	);
        listaIdPollsAnswer.add(	"#QC,LD0-I="	);
        listaIdPollsAnswer.add(	"+>X#UK?,3Y"	);
        listaIdPollsAnswer.add(	"/8(U2.H&$<"	);
        listaIdPollsAnswer.add(	"52W90NXK;H"	);
        listaIdPollsAnswer.add(	"HMOL(9R9$#"	);
        listaIdPollsAnswer.add(	"IPBBJ?P/IO"	);
        listaIdPollsAnswer.add(	"VX694Q/MU1"	);
        listaIdPollsAnswer.add(	"-#TLL)$6(@"	);
        listaIdPollsAnswer.add(	"IF?;8=ETNI"	);
        listaIdPollsAnswer.add(	"W?'WW+9OD."	);
        listaIdPollsAnswer.add(	"%3,'?//5IW"	);
        listaIdPollsAnswer.add(	"MYTZI&ZT<W"	);
        listaIdPollsAnswer.add(	"NZXC%VV30T"	);
        listaIdPollsAnswer.add(	"GYF)UMK;:K"	);
        listaIdPollsAnswer.add(	"(<L;E&#16S"	);
        listaIdPollsAnswer.add(	"#K,2'9-EZ,"	);
        listaIdPollsAnswer.add(	"?F%5BSW#+'"	);
        listaIdPollsAnswer.add(	"0P&$.8YQ<F"	);
        listaIdPollsAnswer.add(	"Z'TB$/4,:D"	);
        listaIdPollsAnswer.add(	"9RAQFD3>TH"	);
        listaIdPollsAnswer.add(	"+12S?X(29L"	);
        listaIdPollsAnswer.add(	"R4<TQYFE8T"	);
        listaIdPollsAnswer.add(	"ED'U('Q:EK"	);
        listaIdPollsAnswer.add(	"O+&BF*XBT6"	);
        listaIdPollsAnswer.add(	"0E<7T:=IOM"	);
        listaIdPollsAnswer.add(	"13>/+*WX8C"	);
        listaIdPollsAnswer.add(	">JP*>C/?S@"	);
        listaIdPollsAnswer.add(	"AZ.V)8'OIL"	);
        listaIdPollsAnswer.add(	"@HW5M4<J0E"	);
        listaIdPollsAnswer.add(	"9V;N'GHQC>"	);
        listaIdPollsAnswer.add(	"U;E9(<$Q*="	);
        listaIdPollsAnswer.add(	"Y96BK=XVB+"	);
        listaIdPollsAnswer.add(	"%NAF9E1+H<"	);
        listaIdPollsAnswer.add(	"WOXY%FC0X'"	);
        listaIdPollsAnswer.add(	"@PTU?<P5Z>"	);
        listaIdPollsAnswer.add(	"ENHO+9Q>;C"	);
        listaIdPollsAnswer.add(	"C4@=W(-@VQ"	);
        listaIdPollsAnswer.add(	"M7)4K<Z5QJ"	);
        listaIdPollsAnswer.add(	"+S,AB@?9O/"	);
        listaIdPollsAnswer.add(	"T$?F2PF7&F"	);
        listaIdPollsAnswer.add(	"7-+E55;IB7"	);
        listaIdPollsAnswer.add(	"ZP;UM,,@SZ"	);
        listaIdPollsAnswer.add(	"-)&$8/0@L:"	);
        listaIdPollsAnswer.add(	"=M)(;(:<'/"	);
        listaIdPollsAnswer.add(	"?;?BFI&>;Q"	);
        listaIdPollsAnswer.add(	"S#FV7BD.DC"	);
        listaIdPollsAnswer.add(	"57NAEW,ZX-"	);
        listaIdPollsAnswer.add(	"&8I++@UXZI"	);
        listaIdPollsAnswer.add(	"GNM@82C60F"	);
        listaIdPollsAnswer.add(	"@<M*8/@;CM"	);
        listaIdPollsAnswer.add(	"TU7<74@#88"	);
        listaIdPollsAnswer.add(	"#:,<GLYW%1"	);
        listaIdPollsAnswer.add(	"T0R/NQ196="	);
        listaIdPollsAnswer.add(	"0'OHG((H#)"	);
        listaIdPollsAnswer.add(	"*3?4)81CSV"	);
        listaIdPollsAnswer.add(	"M#B8II8&-M"	);
        listaIdPollsAnswer.add(	"4)SCZ*'RX1"	);
        listaIdPollsAnswer.add(	"++'=%3OP%W"	);
        listaIdPollsAnswer.add(	"@1LQUD%),V"	);
        listaIdPollsAnswer.add(	"+KKZGP'IEK"	);
        listaIdPollsAnswer.add(	"Y#/Q'3L$#W"	);
        listaIdPollsAnswer.add(	",EMUZ26@$T"	);
        listaIdPollsAnswer.add(	";$NHXZ)37>"	);
        listaIdPollsAnswer.add(	"$0#PKR.U6L"	);
        listaIdPollsAnswer.add(	":NJA'9EVCR"	);
        listaIdPollsAnswer.add(	"5$(JTJ$I0M"	);
        listaIdPollsAnswer.add(	"68<*75OI'$"	);
        listaIdPollsAnswer.add(	"J8?F(00XI)"	);
        listaIdPollsAnswer.add(	"2X(Z@<2$9C"	);
        listaIdPollsAnswer.add(	"X%E=SK*?*I"	);
        listaIdPollsAnswer.add(	"A@N2=4ZUFO"	);
        listaIdPollsAnswer.add(	"Y.I%-,IRRF"	);
        listaIdPollsAnswer.add(	"YMYD8K0P5+"	);
        listaIdPollsAnswer.add(	"CD'&K6QR7("	);
        listaIdPollsAnswer.add(	"G>3(9&%DU-"	);
        listaIdPollsAnswer.add(	"-FK9R?-?@M"	);
        listaIdPollsAnswer.add(	"M$V01&$?F."	);
        listaIdPollsAnswer.add(	"L$C$,I#@1#"	);
        listaIdPollsAnswer.add(	"$QD:&DX@L&"	);
        listaIdPollsAnswer.add(	"R7W<NE6OR-"	);
        listaIdPollsAnswer.add(	"@&G6K5+@#D"	);
        listaIdPollsAnswer.add(	"UE'6F'+UJ6"	);
        listaIdPollsAnswer.add(	"BS.,#O&3YY"	);
        listaIdPollsAnswer.add(	"?-LY.A/D;K"	);
        listaIdPollsAnswer.add(	"G(NP.,W?0,"	);
        listaIdPollsAnswer.add(	"H.1+X%N<QW"	);
        listaIdPollsAnswer.add(	"83$N<GJG/A"	);
        listaIdPollsAnswer.add(	"Z)BDEZWL#4"	);
        listaIdPollsAnswer.add(	"THD4I&E&29"	);
        listaIdPollsAnswer.add(	"5LY=CTJ-=H"	);
        listaIdPollsAnswer.add(	"Q=#9Q6S;.>"	);
        listaIdPollsAnswer.add(	"CCBZ&K.Q%;"	);
        listaIdPollsAnswer.add(	"L9M7SLC>HY"	);
        listaIdPollsAnswer.add(	"TMM#2G@916"	);
        listaIdPollsAnswer.add(	"INYU4$/X9<"	);
        listaIdPollsAnswer.add(	"+K'G=<V,0D"	);
        listaIdPollsAnswer.add(	".MC4)$@*02"	);
        listaIdPollsAnswer.add(	"9BMEN@RLD4"	);
        listaIdPollsAnswer.add(	"LGXI'1=U.'"	);
        listaIdPollsAnswer.add(	"IC3#&/R,<("	);
        listaIdPollsAnswer.add(	"UMP7#B)4HW"	);
        listaIdPollsAnswer.add(	"FNS.%/,EF4"	);
        listaIdPollsAnswer.add(	".4$1/ZI3BQ"	);
        listaIdPollsAnswer.add(	"R+<.0UV6&W"	);
        listaIdPollsAnswer.add(	"NV#C9LJI+/"	);
        listaIdPollsAnswer.add(	"-M;8:.K&#C"	);
        listaIdPollsAnswer.add(	"(GQ=ZD)DO*"	);
        listaIdPollsAnswer.add(	"W95<$>8UZ3"	);
        listaIdPollsAnswer.add(	",;H)KSS$IN"	);
        listaIdPollsAnswer.add(	"ISAZXLPTH;"	);
        listaIdPollsAnswer.add(	"IC/%EC))K'"	);
        listaIdPollsAnswer.add(	"*T,#FN0-$#"	);
        listaIdPollsAnswer.add(	"0X7Q9LXA#P"	);
        listaIdPollsAnswer.add(	"'):0NFWGBZ"	);
        listaIdPollsAnswer.add(	"/#AK9Y,BWA"	);
        listaIdPollsAnswer.add(	"LA9B?X)A:W"	);
        listaIdPollsAnswer.add(	"UZ2&X(7;O3"	);
        listaIdPollsAnswer.add(	"0E(VA/=6($"	);
        listaIdPollsAnswer.add(	"R6$,2/F>M<"	);
        listaIdPollsAnswer.add(	"6?'GBY$(2O"	);
        listaIdPollsAnswer.add(	"'S<9'IL$:'"	);
        listaIdPollsAnswer.add(	"0MUJWX9D/3"	);
        listaIdPollsAnswer.add(	"7Q36S1I&?."	);
        listaIdPollsAnswer.add(	"IZZMO5O'GN"	);
        listaIdPollsAnswer.add(	"<R/-Y.OVKB"	);
        listaIdPollsAnswer.add(	"DE+4AIK8BS"	);
        listaIdPollsAnswer.add(	"R*TY/:TX38"	);
        listaIdPollsAnswer.add(	"*.*SC?-N.T"	);
        listaIdPollsAnswer.add(	"*D1,Z*F?N+"	);
        listaIdPollsAnswer.add(	":I@L8B-NR1"	);
        listaIdPollsAnswer.add(	"VO@?K*0FL*"	);
        listaIdPollsAnswer.add(	"U>>YW'85ZR"	);
        listaIdPollsAnswer.add(	"KF<M4$R0+>"	);
        listaIdPollsAnswer.add(	"/1(?YY@$I?"	);
        listaIdPollsAnswer.add(	"+WLG'V1L3S"	);
        listaIdPollsAnswer.add(	"KLAMFV&MAF"	);
        listaIdPollsAnswer.add(	"<OB+9LN9'Q"	);
        listaIdPollsAnswer.add(	"DMUUV/$&>W"	);
        listaIdPollsAnswer.add(	"TOG$CYMC,$"	);
        listaIdPollsAnswer.add(	"MU*E09TNP/"	);
        listaIdPollsAnswer.add(	"-QGP3=IPRM"	);
        listaIdPollsAnswer.add(	"XF5MF?S&,2"	);
        listaIdPollsAnswer.add(	"R#,9JG35*;"	);
        listaIdPollsAnswer.add(	"XV*AC=ATW#"	);
        listaIdPollsAnswer.add(	"WT:KAZ;45#"	);
        listaIdPollsAnswer.add(	")DG-#784$C"	);
        listaIdPollsAnswer.add(	"0TSZ,FI''8"	);
        listaIdPollsAnswer.add(	"%Z.Q</%QW/"	);
        listaIdPollsAnswer.add(	"-)LG,G5NI6"	);
        listaIdPollsAnswer.add(	"B6D#N7@(R9"	);
        listaIdPollsAnswer.add(	"4%HDNV?G'0"	);
        listaIdPollsAnswer.add(	"69V?/8I7)#"	);
        listaIdPollsAnswer.add(	".D3://Y$%/"	);
        listaIdPollsAnswer.add(	"R*IE5/O%U1"	);
        listaIdPollsAnswer.add(	".T9K-COPR7"	);
        listaIdPollsAnswer.add(	"XZ&(@'&0YV"	);
        listaIdPollsAnswer.add(	"E/41OR>6YR"	);
        listaIdPollsAnswer.add(	"#.'.D@#HOX"	);
        listaIdPollsAnswer.add(	"W=T%CHJ8%B"	);
        listaIdPollsAnswer.add(	":D&74FCVEE"	);
        listaIdPollsAnswer.add(	"0'+E(VY2=0"	);
        listaIdPollsAnswer.add(	"*3L.7,3G#>"	);
        listaIdPollsAnswer.add(	")+V*TF<DK&"	);
        listaIdPollsAnswer.add(	".FU/%(?$BA"	);
        listaIdPollsAnswer.add(	"<42Z*JU0M<"	);
        listaIdPollsAnswer.add(	">NK(W.Y?4Y"	);
        listaIdPollsAnswer.add(	"3BV$N'X4CE"	);
        listaIdPollsAnswer.add(	";1<#N5B$J/"	);
        listaIdPollsAnswer.add(	"#QGW;UAH.*"	);
        listaIdPollsAnswer.add(	"<6ZKJ1?@1B"	);
        listaIdPollsAnswer.add(	"BLDJKQ@S'E"	);
        listaIdPollsAnswer.add(	"TYC'G68)KG"	);
        listaIdPollsAnswer.add(	"L*GJRI*10$"	);
        listaIdPollsAnswer.add(	"/HF/QTW467"	);
        listaIdPollsAnswer.add(	".)FYE'GH&2"	);
        listaIdPollsAnswer.add(	"25+9S./,XO"	);
        listaIdPollsAnswer.add(	"VAK?#@-Z:*"	);
        listaIdPollsAnswer.add(	"6N=UE90WBO"	);
        listaIdPollsAnswer.add(	"OV.C*79:9S"	);
        listaIdPollsAnswer.add(	"4S*&>ZDU8("	);
        listaIdPollsAnswer.add(	"5@8B(974--"	);
        listaIdPollsAnswer.add(	":T7*//R&FG"	);
        listaIdPollsAnswer.add(	"*+>9#F$U-F"	);
        listaIdPollsAnswer.add(	"V1C4GG'Q03"	);
        listaIdPollsAnswer.add(	">4?>C0=<$0"	);
        listaIdPollsAnswer.add(	"-YJ.Z9#ZW>"	);
        listaIdPollsAnswer.add(	">7?TV@D/9W"	);
        listaIdPollsAnswer.add(	"AR(07-7JUK"	);
        listaIdPollsAnswer.add(	"A<.5>Z(;9-"	);
        listaIdPollsAnswer.add(	"Z#O&;TO%A#"	);
        listaIdPollsAnswer.add(	"/I$68X$PK?"	);
        listaIdPollsAnswer.add(	"C/<VM:$0<R"	);
        listaIdPollsAnswer.add(	"*7?PY$B4?J"	);
        listaIdPollsAnswer.add(	"*/WTZ.F/D#"	);
        listaIdPollsAnswer.add(	"'CY@LS(+1>"	);
        listaIdPollsAnswer.add(	"J=<#85UMF-"	);
        listaIdPollsAnswer.add(	"4S-:?9NB08"	);
        listaIdPollsAnswer.add(	"Y#L;GCHYIL"	);
        listaIdPollsAnswer.add(	"I7+8?PG%XZ"	);
        listaIdPollsAnswer.add(	"B/5@,'5>EZ"	);
        listaIdPollsAnswer.add(	"#44%C(:4S="	);
        listaIdPollsAnswer.add(	"LC?4>(P<(K"	);
        listaIdPollsAnswer.add(	"7*(4*5Z&B*"	);
        listaIdPollsAnswer.add(	"IQNF)/%+*#"	);
        listaIdPollsAnswer.add(	"@D.%9FV-+A"	);
        listaIdPollsAnswer.add(	"K.U%5GO(#H"	);
        listaIdPollsAnswer.add(	"JN++'U.HL("	);
        listaIdPollsAnswer.add(	"*(#.L5?#U;"	);
        listaIdPollsAnswer.add(	";HY9ZHS)8H"	);
        listaIdPollsAnswer.add(	"7-PO%=<20/"	);
        listaIdPollsAnswer.add(	"*H/8K'I52B"	);
        listaIdPollsAnswer.add(	"Z$T+%STL94"	);
        listaIdPollsAnswer.add(	"@AZAIJ46)Y"	);
        listaIdPollsAnswer.add(	"2%?509$:?."	);
        listaIdPollsAnswer.add(	"Z64BX5CU8K"	);
        listaIdPollsAnswer.add(	"7;2&V)P7>Q"	);
        listaIdPollsAnswer.add(	"G)H5.5CXAY"	);
        listaIdPollsAnswer.add(	"GK&'R4?;#1"	);
        listaIdPollsAnswer.add(	"S9X8->)1B2"	);
        listaIdPollsAnswer.add(	"#<L(C2:JRR"	);
        listaIdPollsAnswer.add(	"W9DOP-E<LD"	);
        listaIdPollsAnswer.add(	"EJ%T/F%+-H"	);
        listaIdPollsAnswer.add(	"J%3OZ96FR)"	);
        listaIdPollsAnswer.add(	"O&<-)/O/69"	);
        listaIdPollsAnswer.add(	"'8FO+(RSMW"	);
        listaIdPollsAnswer.add(	"Q/-?#)SQNE"	);
        listaIdPollsAnswer.add(	"J,8$.(XBV1"	);
        listaIdPollsAnswer.add(	"L'IIV9>@-N"	);
        listaIdPollsAnswer.add(	"/:Q-18#7N;"	);
        listaIdPollsAnswer.add(	"FE4RL'*N.)"	);
        listaIdPollsAnswer.add(	"87&PYC5U;W"	);
        listaIdPollsAnswer.add(	"%1.DAJP:$P"	);
        listaIdPollsAnswer.add(	"6=F=9M.7LU"	);
        listaIdPollsAnswer.add(	"?JCQ-+<R8>"	);
        listaIdPollsAnswer.add(	"F4&*>=5L04"	);
        listaIdPollsAnswer.add(	"=$A,2A8IE4"	);
        listaIdPollsAnswer.add(	"?7:HM<H.<?"	);
        listaIdPollsAnswer.add(	"I;TL?DK8+9"	);
        listaIdPollsAnswer.add(	"P;U7574B/4"	);
        listaIdPollsAnswer.add(	"YI&(QNI6$U"	);
        listaIdPollsAnswer.add(	"0W?D@YNZGI"	);
        listaIdPollsAnswer.add(	"EA03J=86O6"	);
        listaIdPollsAnswer.add(	"5T@)TC?LFE"	);
        listaIdPollsAnswer.add(	"A'X<K#>RKM"	);
        listaIdPollsAnswer.add(	"O?HI4FACU%"	);
        listaIdPollsAnswer.add(	"*VCPX'7%3I"	);
        listaIdPollsAnswer.add(	"C-$/X319@E"	);
        listaIdPollsAnswer.add(	"-IHO86+9EP"	);
        listaIdPollsAnswer.add(	"TDPIZ.I>S."	);
        listaIdPollsAnswer.add(	"Y'592+B?<?"	);
        listaIdPollsAnswer.add(	"L7..*X52RQ"	);
        listaIdPollsAnswer.add(	"K<M-O4(N.)"	);
        listaIdPollsAnswer.add(	"5E1I8&<-6G"	);
        listaIdPollsAnswer.add(	"#@Q*+6FTIZ"	);
        listaIdPollsAnswer.add(	"+D0S9FT/'W"	);
        listaIdPollsAnswer.add(	"3%FJAOJR/D"	);
        listaIdPollsAnswer.add(	"=+XH8+D60E"	);
        listaIdPollsAnswer.add(	")C5B/+4HR$"	);
        listaIdPollsAnswer.add(	"JZ)MOSN=-<"	);
        listaIdPollsAnswer.add(	"&%5=S$H<-V"	);
        listaIdPollsAnswer.add(	"IW.'K$E<7?"	);
        listaIdPollsAnswer.add(	"N;#)I&G7C$"	);
        listaIdPollsAnswer.add(	"3X4$I,U.Y&"	);
        listaIdPollsAnswer.add(	"4.4@<RV+V>"	);
        listaIdPollsAnswer.add(	"-'.MN/7@=D"	);
        listaIdPollsAnswer.add(	".0.RH@ML&X"	);
        listaIdPollsAnswer.add(	"(>TACAB;WH"	);
        listaIdPollsAnswer.add(	"6,E#)>VKMF"	);
        listaIdPollsAnswer.add(	"*C%BAUCZEX"	);
        listaIdPollsAnswer.add(	"++CMS9=#6."	);
        listaIdPollsAnswer.add(	"Y7:K4',/RL"	);
        listaIdPollsAnswer.add(	"-=7<LO/*I8"	);
        listaIdPollsAnswer.add(	"/JICJIUCXG"	);
        listaIdPollsAnswer.add(	"S?8,WJV'*V"	);
        listaIdPollsAnswer.add(	"BFG<W@D5;B"	);
        listaIdPollsAnswer.add(	"5*JAQ./W>6"	);
        listaIdPollsAnswer.add(	"O($-KOOURE"	);
        listaIdPollsAnswer.add(	"V@Y@B$%SPI"	);
        listaIdPollsAnswer.add(	"<BBM@4S*4@"	);
        listaIdPollsAnswer.add(	"FF1QMT<>T+"	);
        listaIdPollsAnswer.add(	"(AYCK,UTB?"	);
        listaIdPollsAnswer.add(	":YO;IN6JQN"	);
        listaIdPollsAnswer.add(	".SZSI<4I.:"	);
        listaIdPollsAnswer.add(	"OP-F35V,.+"	);
        listaIdPollsAnswer.add(	"11?<=6&VAI"	);
        listaIdPollsAnswer.add(	"P/78O(X8S1"	);
        listaIdPollsAnswer.add(	"J'8*>Q>8)-"	);
        listaIdPollsAnswer.add(	"9?.KYP&;*="	);
        listaIdPollsAnswer.add(	">X.%++O)A&"	);
        listaIdPollsAnswer.add(	"YF24@FWQZ/"	);
        listaIdPollsAnswer.add(	"#74'XN*:NU"	);
        listaIdPollsAnswer.add(	"ULYMJ69U-@"	);
        listaIdPollsAnswer.add(	"T4X@;U1%>M"	);
        listaIdPollsAnswer.add(	"G3VC&)&ZQZ"	);
        listaIdPollsAnswer.add(	"1?LR<#P1XJ"	);
        listaIdPollsAnswer.add(	"O9@BQ8FMZK"	);
        listaIdPollsAnswer.add(	"E0YYEH)(TC"	);
        listaIdPollsAnswer.add(	")NY#/;75+7"	);
        listaIdPollsAnswer.add(	"C@OHG2>X0$"	);
        listaIdPollsAnswer.add(	"5OI?66.06&"	);
        listaIdPollsAnswer.add(	"NQ@WWUKW0Y"	);
        listaIdPollsAnswer.add(	"TC6S55KX0-"	);
        listaIdPollsAnswer.add(	"7=#/(A@-BM"	);
        listaIdPollsAnswer.add(	"$6EG;5>ZLX"	);
        listaIdPollsAnswer.add(	",VPQGYO0'@"	);
        listaIdPollsAnswer.add(	"P@VS2=V,%."	);
        listaIdPollsAnswer.add(	"7>/FZV@E)>"	);
        listaIdPollsAnswer.add(	"OPAY=P?G>K"	);
        listaIdPollsAnswer.add(	"Q5UDY''G9R"	);
        listaIdPollsAnswer.add(	"Q6OK%D//6;"	);
        listaIdPollsAnswer.add(	"?0?1C@FQ*#"	);
        listaIdPollsAnswer.add(	"7V*$)ED#<="	);
        listaIdPollsAnswer.add(	"EJSR#R;,EG"	);
        listaIdPollsAnswer.add(	"$AIE8Y7IV&"	);
        listaIdPollsAnswer.add(	"C$QC&N.S3-"	);
        listaIdPollsAnswer.add(	"X@#A@,I5GC"	);
        listaIdPollsAnswer.add(	"AB2P9L3$TD"	);
        listaIdPollsAnswer.add(	"N>XY&%J)Q%"	);
        listaIdPollsAnswer.add(	"9K(-7;MIUL"	);
        listaIdPollsAnswer.add(	"@SFT.Z-.L("	);
        listaIdPollsAnswer.add(	"6GHO/*26J)"	);
        listaIdPollsAnswer.add(	"QWQ3?8ID6K"	);
        listaIdPollsAnswer.add(	"3NO)V1(>U("	);
        listaIdPollsAnswer.add(	"CM,)IJMPHF"	);
        listaIdPollsAnswer.add(	"M;*:U'M#2G"	);
        listaIdPollsAnswer.add(	"3N4&(/.4SV"	);
        listaIdPollsAnswer.add(	"'*+I7P&V)'"	);
        listaIdPollsAnswer.add(	"D=2=S=1I-D"	);
        listaIdPollsAnswer.add(	"7*=FCAFRP0"	);
        listaIdPollsAnswer.add(	"DY.4N.3=#*"	);
        listaIdPollsAnswer.add(	"(/IF#B)HL2"	);
        listaIdPollsAnswer.add(	"@L3HRL;JZ2"	);
        listaIdPollsAnswer.add(	"S)SMBGV0SS"	);
        listaIdPollsAnswer.add(	"%=PNOEFA0("	);
        listaIdPollsAnswer.add(	"2+%PFCG;V5"	);
        listaIdPollsAnswer.add(	"9/PV@FA#QU"	);
        listaIdPollsAnswer.add(	":D(7*M36KB"	);
        listaIdPollsAnswer.add(	";2$Y.3;OR:"	);
        listaIdPollsAnswer.add(	"WU&CW@O=WP"	);
        listaIdPollsAnswer.add(	"G@:+=&?4>4"	);
        listaIdPollsAnswer.add(	"@-N*V&CVMJ"	);
        listaIdPollsAnswer.add(	".=?8A<T/2H"	);
        listaIdPollsAnswer.add(	"CI5'%5?*%$"	);
        listaIdPollsAnswer.add(	"1:IWS'J:HR"	);
        listaIdPollsAnswer.add(	"4B@T2LZ>54"	);
        listaIdPollsAnswer.add(	"'V<J&JTU+R"	);
        listaIdPollsAnswer.add(	"(HQJTUZL+Z"	);
        listaIdPollsAnswer.add(	"%&IB,W7O+C"	);
        listaIdPollsAnswer.add(	"X2SOE@U>BS"	);
        listaIdPollsAnswer.add(	"NQKW%@K2%,"	);
        listaIdPollsAnswer.add(	"9S$HV:(W3?"	);
        listaIdPollsAnswer.add(	"+N/B#>5Z-1"	);
        listaIdPollsAnswer.add(	"J+P-WC>B%P"	);
        listaIdPollsAnswer.add(	"G4S1F5@>OH"	);
        listaIdPollsAnswer.add(	"PM/R&9'G4T"	);
        listaIdPollsAnswer.add(	"-5WON$P:3>"	);
        listaIdPollsAnswer.add(	"WR/ZV9*0@Z"	);
        listaIdPollsAnswer.add(	"YJ.7OT*-XD"	);
        listaIdPollsAnswer.add(	"OJCV*AJ+<0"	);
        listaIdPollsAnswer.add(	"81+.D?<;;9"	);
        listaIdPollsAnswer.add(	";G#?X96VL@"	);
        listaIdPollsAnswer.add(	"2-CY:3+19;"	);
        listaIdPollsAnswer.add(	"5W;,AH?#SO"	);
        listaIdPollsAnswer.add(	"01W)IS*3$>"	);
        listaIdPollsAnswer.add(	"BU2YM938K,"	);
        listaIdPollsAnswer.add(	"T$0XRP&D+'"	);
        listaIdPollsAnswer.add(	"NFX2EHMSXQ"	);
        listaIdPollsAnswer.add(	"P9(>Q?.3UY"	);
        listaIdPollsAnswer.add(	"2*NWM,%,KM"	);
        listaIdPollsAnswer.add(	"LJ*7VFMEB&"	);
        listaIdPollsAnswer.add(	"-N0X,FKMWC"	);
        listaIdPollsAnswer.add(	"R39UZGDXF<"	);
        listaIdPollsAnswer.add(	"5WUQBGPSI."	);
        listaIdPollsAnswer.add(	"O.VK46TR7H"	);
        listaIdPollsAnswer.add(	"#XN2O-$5VM"	);
        listaIdPollsAnswer.add(	"&;&T(AS&-0"	);
        listaIdPollsAnswer.add(	"ACA?+8BO'*"	);
        listaIdPollsAnswer.add(	"9@K+S&>$6Z"	);
        listaIdPollsAnswer.add(	"E$-7M&J78?"	);
        listaIdPollsAnswer.add(	":N)0@J9X/1"	);
        listaIdPollsAnswer.add(	"P-Z%O2;SM-"	);
        listaIdPollsAnswer.add(	"54.M0&0T3Q"	);
        listaIdPollsAnswer.add(	"HUDH(/K8C'"	);
        listaIdPollsAnswer.add(	"F3)2;WV)J."	);
        listaIdPollsAnswer.add(	"0X1?2&E(RU"	);
        listaIdPollsAnswer.add(	"O7B<MWGW1("	);
        listaIdPollsAnswer.add(	"$S*KIA3OL<"	);
        listaIdPollsAnswer.add(	"%1BHI*9I/)"	);
        listaIdPollsAnswer.add(	"GI$28/?5#2"	);
        listaIdPollsAnswer.add(	"OPD'VXF##-"	);
        listaIdPollsAnswer.add(	"AA-Y,>#PJC"	);
        listaIdPollsAnswer.add(	"=OG4@/=DZ1"	);
        listaIdPollsAnswer.add(	"E?<3WY=I24"	);
        listaIdPollsAnswer.add(	"6DIKEYF2I="	);
        listaIdPollsAnswer.add(	"F%8T,$%VO%"	);
        listaIdPollsAnswer.add(	":K1G*(H*4X"	);
        listaIdPollsAnswer.add(	"UPUXUY/LLT"	);
        listaIdPollsAnswer.add(	"VE=?XF$XDN"	);
        listaIdPollsAnswer.add(	"&=&Q(3Z:B'"	);
        listaIdPollsAnswer.add(	":-M-6?LTS."	);
        listaIdPollsAnswer.add(	"K'*)@;I959"	);
        listaIdPollsAnswer.add(	"+7.9L;<T'6"	);
        listaIdPollsAnswer.add(	"Z2,B93:XB&"	);
        listaIdPollsAnswer.add(	"<Q?R1)T3.C"	);
        listaIdPollsAnswer.add(	"OS=.&JXXU%"	);
        listaIdPollsAnswer.add(	"<6YUMRV;D$"	);
        listaIdPollsAnswer.add(	",.LLH1).;X"	);
        listaIdPollsAnswer.add(	"0OXJJ(-0GA"	);
        listaIdPollsAnswer.add(	"%;B+UI+FI@"	);
        listaIdPollsAnswer.add(	",<E=EZJ<G("	);
        listaIdPollsAnswer.add(	"8S)O3V13LS"	);
        listaIdPollsAnswer.add(	"<UZCS9-D=P"	);
        listaIdPollsAnswer.add(	"-DO/L$'+:I"	);
        listaIdPollsAnswer.add(	"T5CW0&.-)S"	);
        listaIdPollsAnswer.add(	"=JCNNI9F1("	);
        listaIdPollsAnswer.add(	"5.&*AF.R=<"	);
        listaIdPollsAnswer.add(	"BD#WL2=4ZN"	);
        listaIdPollsAnswer.add(	"9)K:6HWX%O"	);
        listaIdPollsAnswer.add(	")J,=52W?=3"	);
        listaIdPollsAnswer.add(	"WL*T6,9SZ9"	);
        listaIdPollsAnswer.add(	">ZRLW=O%T8"	);
        listaIdPollsAnswer.add(	"L<@LU7D1@X"	);
        listaIdPollsAnswer.add(	"'.V%Q+M=Z)"	);
        listaIdPollsAnswer.add(	"KE,06/F<J;"	);
        listaIdPollsAnswer.add(	"A0'AZZY:(@"	);
        listaIdPollsAnswer.add(	"=7E<%Z'A1:"	);
        listaIdPollsAnswer.add(	"E8AG4;Y(KP"	);
        listaIdPollsAnswer.add(	"BE6G?Z<$(6"	);
        listaIdPollsAnswer.add(	"-A;?E94FF2"	);
        listaIdPollsAnswer.add(	"58>+JZ5D=$"	);
        listaIdPollsAnswer.add(	"DKKG,I,(6P"	);
        listaIdPollsAnswer.add(	"4R/F7+>$'B"	);
        listaIdPollsAnswer.add(	"+R4@*2(4WD"	);
        listaIdPollsAnswer.add(	"=MD0/BJXY2"	);
        listaIdPollsAnswer.add(	">(R)MBK/UQ"	);
        listaIdPollsAnswer.add(	"%Y=R+O8N#0"	);
        listaIdPollsAnswer.add(	"TBG=V)'C2>"	);
        listaIdPollsAnswer.add(	"KDT0?LP>7?"	);
        listaIdPollsAnswer.add(	"K3DW6(&&8V"	);
        listaIdPollsAnswer.add(	"J.@C&IQ540"	);
        listaIdPollsAnswer.add(	"1%V#OJEPL#"	);
        listaIdPollsAnswer.add(	"D:2NG#.-MM"	);
        listaIdPollsAnswer.add(	":RYCAHA'T*"	);
        listaIdPollsAnswer.add(	".$7+'S2W'0"	);
        listaIdPollsAnswer.add(	"A9-T3D0?Q)"	);
        listaIdPollsAnswer.add(	"$MOZ9COBIM"	);
        listaIdPollsAnswer.add(	"6=O3;6'(WI"	);
        listaIdPollsAnswer.add(	"29LLCO/L?I"	);
        listaIdPollsAnswer.add(	"F/)X:WSU@N"	);
        listaIdPollsAnswer.add(	"2SMHF*36<%"	);
        listaIdPollsAnswer.add(	"ZBK&%,(>+B"	);
        listaIdPollsAnswer.add(	"%*GAU&T9D)"	);
        listaIdPollsAnswer.add(	"66H>QQ>X/-"	);
        listaIdPollsAnswer.add(	"Y;:Q<I'901"	);
        listaIdPollsAnswer.add(	"@T.5)U>B(&"	);
        listaIdPollsAnswer.add(	"#FNKYD07X4"	);
        listaIdPollsAnswer.add(	"2R9BMX;)B'"	);
        listaIdPollsAnswer.add(	"*Y@@L')YO4"	);
        listaIdPollsAnswer.add(	"%-SMBHWL95"	);
        listaIdPollsAnswer.add(	"/XWXBCECA="	);
        listaIdPollsAnswer.add(	"67T2Q9S'*+"	);
        listaIdPollsAnswer.add(	"8/9FJ'3H?N"	);
        listaIdPollsAnswer.add(	"QD9%S<I<-3"	);
        listaIdPollsAnswer.add(	"4O4(BJ'O;D"	);
        listaIdPollsAnswer.add(	"*6CZCE0?=&"	);
        listaIdPollsAnswer.add(	"@)R1*>7S?/"	);
        listaIdPollsAnswer.add(	")9LNF9,#7U"	);
        listaIdPollsAnswer.add(	"UV$/>(BP0O"	);
        listaIdPollsAnswer.add(	"0D#P@?QO,G"	);
        listaIdPollsAnswer.add(	"**/$-Z#4*;"	);
        listaIdPollsAnswer.add(	"E-5-V:FT+X"	);
        listaIdPollsAnswer.add(	"4,JPU;8A3C"	);
        listaIdPollsAnswer.add(	"O@Q-LK/M86"	);
        listaIdPollsAnswer.add(	"N3R:X#KGOD"	);
        listaIdPollsAnswer.add(	"L(VXFCGSO:"	);
        listaIdPollsAnswer.add(	"+,U<0$JS9S"	);
        listaIdPollsAnswer.add(	"8GCO/W(VHU"	);
        listaIdPollsAnswer.add(	"64J%@:>=P."	);
        listaIdPollsAnswer.add(	"B,VS.K=D%?"	);
        listaIdPollsAnswer.add(	"SCI2LCI<X<"	);
        listaIdPollsAnswer.add(	"#5$Q-@5X>P"	);
        listaIdPollsAnswer.add(	"60JD/035YD"	);
        listaIdPollsAnswer.add(	"C3X2:G9;WU"	);
        listaIdPollsAnswer.add(	"N5D>JPPJ-:"	);
        listaIdPollsAnswer.add(	"E@GOUFO3,)"	);
        listaIdPollsAnswer.add(	"9ED5BM#&(T"	);
        listaIdPollsAnswer.add(	"I$+I;A$,*G"	);
        listaIdPollsAnswer.add(	"89R<1YUFZ1"	);
        listaIdPollsAnswer.add(	"%9KA/5A4CP"	);
        listaIdPollsAnswer.add(	"CP29K.>R-:"	);
        listaIdPollsAnswer.add(	"*)7?6F8DVJ"	);
        listaIdPollsAnswer.add(	"NY%+8FSDEG"	);
        listaIdPollsAnswer.add(	"'#OKR2P25D"	);
        listaIdPollsAnswer.add(	".XDLWEQCE3"	);
        listaIdPollsAnswer.add(	"7YGB*,=H<&"	);
        listaIdPollsAnswer.add(	"2/FVOJFP$R"	);
        listaIdPollsAnswer.add(	".1/>U*R@GP"	);
        listaIdPollsAnswer.add(	"6=S9SBAY3@"	);
        listaIdPollsAnswer.add(	"'80J@N'LL'"	);
        listaIdPollsAnswer.add(	"DGY:OD$=FV"	);
        listaIdPollsAnswer.add(	"9L'E:8V:,<"	);
        listaIdPollsAnswer.add(	"Q8P=2#MC$N"	);
        listaIdPollsAnswer.add(	"Y4><3PN%@S"	);
        listaIdPollsAnswer.add(	"PZTT$U&AB6"	);
        listaIdPollsAnswer.add(	"B%1V+4NDS;"	);
        listaIdPollsAnswer.add(	"N0L-$I5=@5"	);
        listaIdPollsAnswer.add(	"BVG4,U?D+8"	);
        listaIdPollsAnswer.add(	"KW6>,5,JZ,"	);
        listaIdPollsAnswer.add(	"B2>Y??<5<W"	);
        listaIdPollsAnswer.add(	"%%+$4$WB?R"	);
        listaIdPollsAnswer.add(	"M:VIN?J),>"	);
        listaIdPollsAnswer.add(	"K1C%TA+(VI"	);
        listaIdPollsAnswer.add(	"U4*,PC3/P7"	);
        listaIdPollsAnswer.add(	":*$@ATQI:V"	);
        listaIdPollsAnswer.add(	"('BH.%)':O"	);
        listaIdPollsAnswer.add(	"PI&$RYZUAP"	);
        listaIdPollsAnswer.add(	"A'Z#XLAX31"	);
        listaIdPollsAnswer.add(	"<@#I&-WEQ*"	);
        listaIdPollsAnswer.add(	"/K<1NBEZ96"	);
        listaIdPollsAnswer.add(	"G#)-;9Q#9X"	);
        listaIdPollsAnswer.add(	"8ZV8QC&DBE"	);
        listaIdPollsAnswer.add(	")KL=@,WHGB"	);
        listaIdPollsAnswer.add(	"4GWGG;TPJN"	);
        listaIdPollsAnswer.add(	"00$CGB8UT:"	);
        listaIdPollsAnswer.add(	"N@Q.Y;X?E>"	);
        listaIdPollsAnswer.add(	"%?Z+L*52;D"	);
        listaIdPollsAnswer.add(	"QCR:7.CH#."	);
        listaIdPollsAnswer.add(	"?XONG*)<2:"	);
        listaIdPollsAnswer.add(	",=;N=O0PB$"	);
        listaIdPollsAnswer.add(	">$NVI;Z*1-"	);
        listaIdPollsAnswer.add(	"F5=/X4Y?##"	);
        listaIdPollsAnswer.add(	"GC)Y)R4,(;"	);
        listaIdPollsAnswer.add(	"#B1OHOR-TW"	);
        listaIdPollsAnswer.add(	"L)N9>NLJ00"	);
        listaIdPollsAnswer.add(	"UV59T*Z#X&"	);
        listaIdPollsAnswer.add(	"?7%M>ELT,1"	);
        listaIdPollsAnswer.add(	",)8EWSA4(K"	);
        listaIdPollsAnswer.add(	"?FI1RU#C14"	);
        listaIdPollsAnswer.add(	"DU+9L9F+=N"	);
        listaIdPollsAnswer.add(	"HZ33'//TD;"	);
        listaIdPollsAnswer.add(	"@S8-SF(6X*"	);
        listaIdPollsAnswer.add(	"2VY2;<E&N%"	);
        listaIdPollsAnswer.add(	"9Z0K.:Y&IH"	);
        listaIdPollsAnswer.add(	"W&JZ>0;F8@"	);
        listaIdPollsAnswer.add(	"@4N;UE%W0-"	);
        listaIdPollsAnswer.add(	"%O#I<%=9RM"	);
        listaIdPollsAnswer.add(	"CF@@C)RLQ%"	);
        listaIdPollsAnswer.add(	"F%%--/3:#M"	);
        listaIdPollsAnswer.add(	"(R0YC@P4=("	);
        listaIdPollsAnswer.add(	"E$@4&M+$O2"	);
        listaIdPollsAnswer.add(	"+1<U(:D<3;"	);
        listaIdPollsAnswer.add(	"7$CC:)XA1L"	);
        listaIdPollsAnswer.add(	"EZ-N.?.)P8"	);
        listaIdPollsAnswer.add(	"Y),B?I>@T,"	);
        listaIdPollsAnswer.add(	"?<X>T/FEY0"	);
        listaIdPollsAnswer.add(	"@I&EYGD#'8"	);
        listaIdPollsAnswer.add(	"$,#G'6-U7H"	);
        listaIdPollsAnswer.add(	"OT;VB?XV5&"	);
        listaIdPollsAnswer.add(	"Q(PU5##3F$"	);
        listaIdPollsAnswer.add(	"8>GD#-K6MZ"	);
        listaIdPollsAnswer.add(	">7TQX4<:5S"	);
        listaIdPollsAnswer.add(	"COE;EX/:L>"	);
        listaIdPollsAnswer.add(	"GBIH9MPOV-"	);
        listaIdPollsAnswer.add(	"D)=&NVEJR="	);
        listaIdPollsAnswer.add(	"63?V1OF=ZT"	);
        listaIdPollsAnswer.add(	"X;>C.RW1M9"	);
        listaIdPollsAnswer.add(	"LD=18QR<V;"	);

        return listaIdPollsAnswer;
    }
}

/** Clase almacenar listas para Answer PSYCHOSOCIAL **/
class ListasPsychosocial {
    public List<String> listaPollParaPsychosocial(){
        List<String> listaSoloIdPoll = new ArrayList<>();
        listaSoloIdPoll.add(">,U80A6;,S");
        listaSoloIdPoll.add("?=C72EJQ2%");
        listaSoloIdPoll.add("%=K+S?O16X");
        listaSoloIdPoll.add("<X71=BWSXD");
        listaSoloIdPoll.add("B);C08-$@R");
        listaSoloIdPoll.add("H1:GC*E$(B");
        listaSoloIdPoll.add("6IN3Y8NPB3");
        listaSoloIdPoll.add(")3EZUKVK3A");
        listaSoloIdPoll.add("4>LZ0PX.Z0");
        listaSoloIdPoll.add("0/R@W4'B/)");
        listaSoloIdPoll.add("/N5:C:$8$N");
        listaSoloIdPoll.add("MGAC2N#QJ<");
        listaSoloIdPoll.add("'<L)2,JKZ7");
        listaSoloIdPoll.add("RV1&M'8$5F");
        listaSoloIdPoll.add("*RE4Z3=IE-");
        listaSoloIdPoll.add("@K92<:JMYN");
        listaSoloIdPoll.add("C8V<Q03K%N");
        listaSoloIdPoll.add("/KHC6D=))F");
        listaSoloIdPoll.add("NZ,3<C>COI");
        listaSoloIdPoll.add("B6@Z*G:4LR");
        listaSoloIdPoll.add("9F:-%PZ/@#");
        listaSoloIdPoll.add("R#*6$N%VK8");
        listaSoloIdPoll.add(">)'0*0PS'4");
        listaSoloIdPoll.add(">0CYVS=6#E");
        listaSoloIdPoll.add("3R>3L9OZ;;");
        listaSoloIdPoll.add("SR'M4O1?',");
        listaSoloIdPoll.add("9,Z-:OI#XN");
        listaSoloIdPoll.add("#QC,LD0-I=");
        listaSoloIdPoll.add("+>X#UK?,3Y");
        listaSoloIdPoll.add("/8(U2.H&$<");
        listaSoloIdPoll.add("52W90NXK;H");
        listaSoloIdPoll.add("HMOL(9R9$#");
        listaSoloIdPoll.add("IPBBJ?P/IO");
        listaSoloIdPoll.add("VX694Q/MU1");
        listaSoloIdPoll.add("-#TLL)$6(@");
        listaSoloIdPoll.add("IF?;8=ETNI");
        listaSoloIdPoll.add("%3,'?//5IW");
        listaSoloIdPoll.add("GYF)UMK;:K");
        listaSoloIdPoll.add("#K,2'9-EZ,");
        listaSoloIdPoll.add("?F%5BSW#+'");
        listaSoloIdPoll.add("0P&$.8YQ<F");
        listaSoloIdPoll.add("Z'TB$/4,:D");
        listaSoloIdPoll.add("R4<TQYFE8T");
        listaSoloIdPoll.add("0E<7T:=IOM");
        listaSoloIdPoll.add("13>/+*WX8C");
        listaSoloIdPoll.add(">JP*>C/?S@");
        listaSoloIdPoll.add("AZ.V)8'OIL");
        listaSoloIdPoll.add("9V;N'GHQC>");
        listaSoloIdPoll.add("U;E9(<$Q*=");
        listaSoloIdPoll.add("Y96BK=XVB+");
        listaSoloIdPoll.add("ENHO+9Q>;C");
        listaSoloIdPoll.add("C4@=W(-@VQ");
        listaSoloIdPoll.add("T$?F2PF7&F");
        listaSoloIdPoll.add("ZP;UM,,@SZ");
        listaSoloIdPoll.add("-)&$8/0@L:");
        listaSoloIdPoll.add("=M)(;(:<'/");
        listaSoloIdPoll.add("?;?BFI&>;Q");
        listaSoloIdPoll.add("S#FV7BD.DC");
        listaSoloIdPoll.add("57NAEW,ZX-");
        listaSoloIdPoll.add("&8I++@UXZI");
        listaSoloIdPoll.add("GNM@82C60F");
        listaSoloIdPoll.add("@<M*8/@;CM");
        listaSoloIdPoll.add("TU7<74@#88");
        listaSoloIdPoll.add("#:,<GLYW%1");
        listaSoloIdPoll.add("T0R/NQ196=");
        listaSoloIdPoll.add("0'OHG((H#)");
        listaSoloIdPoll.add("*3?4)81CSV");
        listaSoloIdPoll.add("M#B8II8&-M");
        listaSoloIdPoll.add("4)SCZ*'RX1");
        listaSoloIdPoll.add("++'=%3OP%W");
        listaSoloIdPoll.add("@1LQUD%),V");
        listaSoloIdPoll.add("+KKZGP'IEK");
        listaSoloIdPoll.add("Y#/Q'3L$#W");
        listaSoloIdPoll.add(",EMUZ26@$T");
        listaSoloIdPoll.add(";$NHXZ)37>");
        listaSoloIdPoll.add("$0#PKR.U6L");
        listaSoloIdPoll.add(":NJA'9EVCR");
        listaSoloIdPoll.add("5$(JTJ$I0M");
        listaSoloIdPoll.add("68<*75OI'$");
        listaSoloIdPoll.add("J8?F(00XI)");
        listaSoloIdPoll.add("2X(Z@<2$9C");
        listaSoloIdPoll.add("X%E=SK*?*I");
        listaSoloIdPoll.add("A@N2=4ZUFO");
        listaSoloIdPoll.add("Y.I%-,IRRF");
        listaSoloIdPoll.add("YMYD8K0P5+");
        listaSoloIdPoll.add("CD'&K6QR7(");
        listaSoloIdPoll.add("G>3(9&%DU-");
        listaSoloIdPoll.add("-FK9R?-?@M");
        listaSoloIdPoll.add("M$V01&$?F.");
        listaSoloIdPoll.add("L$C$,I#@1#");
        listaSoloIdPoll.add("$QD:&DX@L&");
        listaSoloIdPoll.add("R7W<NE6OR-");
        listaSoloIdPoll.add("@&G6K5+@#D");
        listaSoloIdPoll.add("UE'6F'+UJ6");
        listaSoloIdPoll.add("G(NP.,W?0,");
        listaSoloIdPoll.add("Z)BDEZWL#4");
        listaSoloIdPoll.add("5LY=CTJ-=H");
        listaSoloIdPoll.add("CCBZ&K.Q%;");
        listaSoloIdPoll.add("L9M7SLC>HY");
        listaSoloIdPoll.add("+K'G=<V,0D");
        listaSoloIdPoll.add("IC3#&/R,<(");
        listaSoloIdPoll.add("UMP7#B)4HW");
        listaSoloIdPoll.add("FNS.%/,EF4");
        listaSoloIdPoll.add("NV#C9LJI+/");
        listaSoloIdPoll.add("(GQ=ZD)DO*");
        listaSoloIdPoll.add("W95<$>8UZ3");
        listaSoloIdPoll.add("IC/%EC))K'");
        listaSoloIdPoll.add("0X7Q9LXA#P");
        listaSoloIdPoll.add("LA9B?X)A:W");
        listaSoloIdPoll.add("UZ2&X(7;O3");
        listaSoloIdPoll.add("0E(VA/=6($");
        listaSoloIdPoll.add("R6$,2/F>M<");
        listaSoloIdPoll.add("6?'GBY$(2O");
        listaSoloIdPoll.add("'S<9'IL$:'");
        listaSoloIdPoll.add("0MUJWX9D/3");
        listaSoloIdPoll.add("7Q36S1I&?.");
        listaSoloIdPoll.add("IZZMO5O'GN");
        listaSoloIdPoll.add("<R/-Y.OVKB");
        listaSoloIdPoll.add("DE+4AIK8BS");
        listaSoloIdPoll.add("R*TY/:TX38");
        listaSoloIdPoll.add("*.*SC?-N.T");
        listaSoloIdPoll.add("*D1,Z*F?N+");
        listaSoloIdPoll.add(":I@L8B-NR1");
        listaSoloIdPoll.add("VO@?K*0FL*");
        listaSoloIdPoll.add("U>>YW'85ZR");
        listaSoloIdPoll.add("KF<M4$R0+>");
        listaSoloIdPoll.add("/1(?YY@$I?");
        listaSoloIdPoll.add("+WLG'V1L3S");
        listaSoloIdPoll.add("KLAMFV&MAF");
        listaSoloIdPoll.add("<OB+9LN9'Q");
        listaSoloIdPoll.add("DMUUV/$&>W");
        listaSoloIdPoll.add("TOG$CYMC,$");
        listaSoloIdPoll.add("-QGP3=IPRM");
        listaSoloIdPoll.add("XF5MF?S&,2");
        listaSoloIdPoll.add("R#,9JG35*;");
        listaSoloIdPoll.add("XV*AC=ATW#");
        listaSoloIdPoll.add("WT:KAZ;45#");
        listaSoloIdPoll.add(")DG-#784$C");
        listaSoloIdPoll.add("0TSZ,FI''8");
        listaSoloIdPoll.add("-)LG,G5NI6");
        listaSoloIdPoll.add("B6D#N7@(R9");
        listaSoloIdPoll.add("4%HDNV?G'0");
        listaSoloIdPoll.add("69V?/8I7)#");
        listaSoloIdPoll.add(".T9K-COPR7");
        listaSoloIdPoll.add("XZ&(@'&0YV");
        listaSoloIdPoll.add("E/41OR>6YR");
        listaSoloIdPoll.add("#.'.D@#HOX");
        listaSoloIdPoll.add("W=T%CHJ8%B");
        listaSoloIdPoll.add(":D&74FCVEE");
        listaSoloIdPoll.add("0'+E(VY2=0");
        listaSoloIdPoll.add("*3L.7,3G#>");
        listaSoloIdPoll.add(".FU/%(?$BA");
        listaSoloIdPoll.add("<42Z*JU0M<");
        listaSoloIdPoll.add(">NK(W.Y?4Y");
        listaSoloIdPoll.add("3BV$N'X4CE");
        listaSoloIdPoll.add(";1<#N5B$J/");
        listaSoloIdPoll.add("#QGW;UAH.*");
        listaSoloIdPoll.add("<6ZKJ1?@1B");
        listaSoloIdPoll.add("BLDJKQ@S'E");
        listaSoloIdPoll.add("6N=UE90WBO");
        listaSoloIdPoll.add("OV.C*79:9S");
        listaSoloIdPoll.add("4S*&>ZDU8(");
        listaSoloIdPoll.add("5@8B(974--");
        listaSoloIdPoll.add(":T7*//R&FG");
        listaSoloIdPoll.add("*+>9#F$U-F");
        listaSoloIdPoll.add(">7?TV@D/9W");
        listaSoloIdPoll.add("AR(07-7JUK");
        listaSoloIdPoll.add("A<.5>Z(;9-");
        listaSoloIdPoll.add("/I$68X$PK?");
        listaSoloIdPoll.add("'CY@LS(+1>");
        listaSoloIdPoll.add("J=<#85UMF-");
        listaSoloIdPoll.add("4S-:?9NB08");
        listaSoloIdPoll.add("Y#L;GCHYIL");
        listaSoloIdPoll.add("I7+8?PG%XZ");
        listaSoloIdPoll.add("B/5@,'5>EZ");
        listaSoloIdPoll.add("#44%C(:4S=");
        listaSoloIdPoll.add("LC?4>(P<(K");
        listaSoloIdPoll.add("7*(4*5Z&B*");
        listaSoloIdPoll.add(";HY9ZHS)8H");
        listaSoloIdPoll.add("@AZAIJ46)Y");
        listaSoloIdPoll.add("G)H5.5CXAY");
        listaSoloIdPoll.add("EJ%T/F%+-H");
        listaSoloIdPoll.add("J%3OZ96FR)");
        listaSoloIdPoll.add("O&<-)/O/69");
        listaSoloIdPoll.add("'8FO+(RSMW");
        listaSoloIdPoll.add("/:Q-18#7N;");
        listaSoloIdPoll.add("FE4RL'*N.)");
        listaSoloIdPoll.add("87&PYC5U;W");
        listaSoloIdPoll.add("%1.DAJP:$P");
        listaSoloIdPoll.add("6=F=9M.7LU");
        listaSoloIdPoll.add("F4&*>=5L04");
        listaSoloIdPoll.add("I;TL?DK8+9");
        listaSoloIdPoll.add("P;U7574B/4");
        listaSoloIdPoll.add("YI&(QNI6$U");
        listaSoloIdPoll.add("0W?D@YNZGI");
        listaSoloIdPoll.add("EA03J=86O6");
        listaSoloIdPoll.add("5T@)TC?LFE");
        listaSoloIdPoll.add("A'X<K#>RKM");
        listaSoloIdPoll.add("O?HI4FACU%");
        listaSoloIdPoll.add("C-$/X319@E");
        listaSoloIdPoll.add("TDPIZ.I>S.");
        listaSoloIdPoll.add("L7..*X52RQ");
        listaSoloIdPoll.add("K<M-O4(N.)");
        listaSoloIdPoll.add("5E1I8&<-6G");
        listaSoloIdPoll.add("#@Q*+6FTIZ");
        listaSoloIdPoll.add("+D0S9FT/'W");
        listaSoloIdPoll.add("3%FJAOJR/D");
        listaSoloIdPoll.add("=+XH8+D60E");
        listaSoloIdPoll.add(")C5B/+4HR$");
        listaSoloIdPoll.add("N;#)I&G7C$");
        listaSoloIdPoll.add("4.4@<RV+V>");
        listaSoloIdPoll.add("-'.MN/7@=D");
        listaSoloIdPoll.add(".0.RH@ML&X");
        listaSoloIdPoll.add("6,E#)>VKMF");
        listaSoloIdPoll.add("++CMS9=#6.");
        listaSoloIdPoll.add("S?8,WJV'*V");
        listaSoloIdPoll.add("BFG<W@D5;B");
        listaSoloIdPoll.add("5*JAQ./W>6");
        listaSoloIdPoll.add("O($-KOOURE");
        listaSoloIdPoll.add("<BBM@4S*4@");
        listaSoloIdPoll.add("FF1QMT<>T+");
        listaSoloIdPoll.add("(AYCK,UTB?");
        listaSoloIdPoll.add(":YO;IN6JQN");
        listaSoloIdPoll.add(".SZSI<4I.:");
        listaSoloIdPoll.add("OP-F35V,.+");
        listaSoloIdPoll.add("11?<=6&VAI");
        listaSoloIdPoll.add("P/78O(X8S1");
        listaSoloIdPoll.add("J'8*>Q>8)-");
        listaSoloIdPoll.add("9?.KYP&;*=");
        listaSoloIdPoll.add(">X.%++O)A&");
        listaSoloIdPoll.add("YF24@FWQZ/");
        listaSoloIdPoll.add("#74'XN*:NU");
        listaSoloIdPoll.add("ULYMJ69U-@");
        listaSoloIdPoll.add("T4X@;U1%>M");
        listaSoloIdPoll.add("G3VC&)&ZQZ");
        listaSoloIdPoll.add("1?LR<#P1XJ");
        listaSoloIdPoll.add("O9@BQ8FMZK");
        listaSoloIdPoll.add("E0YYEH)(TC");
        listaSoloIdPoll.add(")NY#/;75+7");
        listaSoloIdPoll.add("C@OHG2>X0$");
        listaSoloIdPoll.add("5OI?66.06&");
        listaSoloIdPoll.add("NQ@WWUKW0Y");
        listaSoloIdPoll.add("TC6S55KX0-");
        listaSoloIdPoll.add("7=#/(A@-BM");
        listaSoloIdPoll.add("$6EG;5>ZLX");
        listaSoloIdPoll.add(",VPQGYO0'@");
        listaSoloIdPoll.add("Q5UDY''G9R");
        listaSoloIdPoll.add("7V*$)ED#<=");
        listaSoloIdPoll.add("EJSR#R;,EG");
        listaSoloIdPoll.add("$AIE8Y7IV&");
        listaSoloIdPoll.add("9K(-7;MIUL");
        listaSoloIdPoll.add("@SFT.Z-.L(");
        listaSoloIdPoll.add("6GHO/*26J)");
        listaSoloIdPoll.add("QWQ3?8ID6K");
        listaSoloIdPoll.add("3NO)V1(>U(");
        listaSoloIdPoll.add("CM,)IJMPHF");
        listaSoloIdPoll.add("M;*:U'M#2G");
        listaSoloIdPoll.add("3N4&(/.4SV");
        listaSoloIdPoll.add("'*+I7P&V)'");
        listaSoloIdPoll.add("D=2=S=1I-D");
        listaSoloIdPoll.add("7*=FCAFRP0");
        listaSoloIdPoll.add("DY.4N.3=#*");
        listaSoloIdPoll.add("(/IF#B)HL2");
        listaSoloIdPoll.add("@L3HRL;JZ2");
        listaSoloIdPoll.add("S)SMBGV0SS");
        listaSoloIdPoll.add("%=PNOEFA0(");
        listaSoloIdPoll.add("2+%PFCG;V5");
        listaSoloIdPoll.add("9/PV@FA#QU");
        listaSoloIdPoll.add(":D(7*M36KB");
        listaSoloIdPoll.add(";2$Y.3;OR:");
        listaSoloIdPoll.add("WU&CW@O=WP");
        listaSoloIdPoll.add("G@:+=&?4>4");
        listaSoloIdPoll.add("@-N*V&CVMJ");
        listaSoloIdPoll.add(".=?8A<T/2H");
        listaSoloIdPoll.add("CI5'%5?*%$");
        listaSoloIdPoll.add("1:IWS'J:HR");
        listaSoloIdPoll.add("4B@T2LZ>54");
        listaSoloIdPoll.add("'V<J&JTU+R");
        listaSoloIdPoll.add("(HQJTUZL+Z");
        listaSoloIdPoll.add("%&IB,W7O+C");
        listaSoloIdPoll.add("X2SOE@U>BS");
        listaSoloIdPoll.add("NQKW%@K2%,");
        listaSoloIdPoll.add("9S$HV:(W3?");
        listaSoloIdPoll.add("+N/B#>5Z-1");
        listaSoloIdPoll.add("J+P-WC>B%P");
        listaSoloIdPoll.add("G4S1F5@>OH");
        listaSoloIdPoll.add("PM/R&9'G4T");
        listaSoloIdPoll.add("-5WON$P:3>");
        listaSoloIdPoll.add("WR/ZV9*0@Z");
        listaSoloIdPoll.add("YJ.7OT*-XD");
        listaSoloIdPoll.add("OJCV*AJ+<0");
        listaSoloIdPoll.add("81+.D?<;;9");
        listaSoloIdPoll.add(";G#?X96VL@");
        listaSoloIdPoll.add("2-CY:3+19;");
        listaSoloIdPoll.add("5W;,AH?#SO");
        listaSoloIdPoll.add("01W)IS*3$>");
        listaSoloIdPoll.add("BU2YM938K,");
        listaSoloIdPoll.add("T$0XRP&D+'");
        listaSoloIdPoll.add("NFX2EHMSXQ");
        listaSoloIdPoll.add("P9(>Q?.3UY");
        listaSoloIdPoll.add("2*NWM,%,KM");
        listaSoloIdPoll.add("LJ*7VFMEB&");
        listaSoloIdPoll.add("-N0X,FKMWC");
        listaSoloIdPoll.add("R39UZGDXF<");
        listaSoloIdPoll.add("5WUQBGPSI.");
        listaSoloIdPoll.add("O.VK46TR7H");
        listaSoloIdPoll.add("#XN2O-$5VM");
        listaSoloIdPoll.add("&;&T(AS&-0");
        listaSoloIdPoll.add("ACA?+8BO'*");
        listaSoloIdPoll.add("9@K+S&>$6Z");
        listaSoloIdPoll.add("E$-7M&J78?");
        listaSoloIdPoll.add(":N)0@J9X/1");
        listaSoloIdPoll.add("P-Z%O2;SM-");
        listaSoloIdPoll.add("54.M0&0T3Q");
        listaSoloIdPoll.add("HUDH(/K8C'");
        listaSoloIdPoll.add("F3)2;WV)J.");
        listaSoloIdPoll.add("0X1?2&E(RU");
        listaSoloIdPoll.add("O7B<MWGW1(");
        listaSoloIdPoll.add("$S*KIA3OL<");
        listaSoloIdPoll.add("%1BHI*9I/)");
        listaSoloIdPoll.add("GI$28/?5#2");
        listaSoloIdPoll.add("OPD'VXF##-");
        listaSoloIdPoll.add("AA-Y,>#PJC");
        listaSoloIdPoll.add("=OG4@/=DZ1");
        listaSoloIdPoll.add("E?<3WY=I24");
        listaSoloIdPoll.add("6DIKEYF2I=");
        listaSoloIdPoll.add("F%8T,$%VO%");
        listaSoloIdPoll.add(":K1G*(H*4X");
        listaSoloIdPoll.add("UPUXUY/LLT");
        listaSoloIdPoll.add("&=&Q(3Z:B'");
        listaSoloIdPoll.add(":-M-6?LTS.");
        listaSoloIdPoll.add("K'*)@;I959");
        listaSoloIdPoll.add("+7.9L;<T'6");
        listaSoloIdPoll.add("Z2,B93:XB&");
        listaSoloIdPoll.add("<Q?R1)T3.C");
        listaSoloIdPoll.add("OS=.&JXXU%");
        listaSoloIdPoll.add("<6YUMRV;D$");
        listaSoloIdPoll.add(",.LLH1).;X");
        listaSoloIdPoll.add("%;B+UI+FI@");
        listaSoloIdPoll.add(",<E=EZJ<G(");
        listaSoloIdPoll.add("8S)O3V13LS");
        listaSoloIdPoll.add("<UZCS9-D=P");
        listaSoloIdPoll.add("-DO/L$'+:I");
        listaSoloIdPoll.add("T5CW0&.-)S");
        listaSoloIdPoll.add("=JCNNI9F1(");
        listaSoloIdPoll.add("5.&*AF.R=<");
        listaSoloIdPoll.add("BD#WL2=4ZN");
        listaSoloIdPoll.add("9)K:6HWX%O");
        listaSoloIdPoll.add(")J,=52W?=3");
        listaSoloIdPoll.add("WL*T6,9SZ9");
        listaSoloIdPoll.add(">ZRLW=O%T8");
        listaSoloIdPoll.add("L<@LU7D1@X");
        listaSoloIdPoll.add("'.V%Q+M=Z)");
        listaSoloIdPoll.add("KE,06/F<J;");
        listaSoloIdPoll.add("A0'AZZY:(@");
        listaSoloIdPoll.add("=7E<%Z'A1:");
        listaSoloIdPoll.add("E8AG4;Y(KP");
        listaSoloIdPoll.add("BE6G?Z<$(6");
        listaSoloIdPoll.add("-A;?E94FF2");
        listaSoloIdPoll.add("58>+JZ5D=$");
        listaSoloIdPoll.add("DKKG,I,(6P");
        listaSoloIdPoll.add("4R/F7+>$'B");
        listaSoloIdPoll.add("+R4@*2(4WD");
        listaSoloIdPoll.add(">(R)MBK/UQ");
        listaSoloIdPoll.add("%Y=R+O8N#0");
        listaSoloIdPoll.add("TBG=V)'C2>");
        listaSoloIdPoll.add("KDT0?LP>7?");
        listaSoloIdPoll.add("K3DW6(&&8V");
        listaSoloIdPoll.add("J.@C&IQ540");
        listaSoloIdPoll.add("1%V#OJEPL#");
        listaSoloIdPoll.add("D:2NG#.-MM");
        listaSoloIdPoll.add(":RYCAHA'T*");
        listaSoloIdPoll.add(".$7+'S2W'0");
        listaSoloIdPoll.add("A9-T3D0?Q)");
        listaSoloIdPoll.add("$MOZ9COBIM");
        listaSoloIdPoll.add("6=O3;6'(WI");
        listaSoloIdPoll.add("29LLCO/L?I");
        listaSoloIdPoll.add("F/)X:WSU@N");
        listaSoloIdPoll.add("2SMHF*36<%");
        listaSoloIdPoll.add("ZBK&%,(>+B");
        listaSoloIdPoll.add("%*GAU&T9D)");
        listaSoloIdPoll.add("66H>QQ>X/-");
        listaSoloIdPoll.add("Y;:Q<I'901");
        listaSoloIdPoll.add("@T.5)U>B(&");
        listaSoloIdPoll.add("#FNKYD07X4");
        listaSoloIdPoll.add("2R9BMX;)B'");
        listaSoloIdPoll.add("*Y@@L')YO4");
        listaSoloIdPoll.add("%-SMBHWL95");
        listaSoloIdPoll.add("/XWXBCECA=");
        listaSoloIdPoll.add("67T2Q9S'*+");
        listaSoloIdPoll.add("8/9FJ'3H?N");
        listaSoloIdPoll.add("QD9%S<I<-3");
        listaSoloIdPoll.add("4O4(BJ'O;D");
        listaSoloIdPoll.add("*6CZCE0?=&");
        listaSoloIdPoll.add("@)R1*>7S?/");
        listaSoloIdPoll.add(")9LNF9,#7U");
        listaSoloIdPoll.add("UV$/>(BP0O");
        listaSoloIdPoll.add("0D#P@?QO,G");
        listaSoloIdPoll.add("**/$-Z#4*;");
        listaSoloIdPoll.add("E-5-V:FT+X");
        listaSoloIdPoll.add("4,JPU;8A3C");
        listaSoloIdPoll.add("O@Q-LK/M86");
        listaSoloIdPoll.add("N3R:X#KGOD");
        listaSoloIdPoll.add("L(VXFCGSO:");
        listaSoloIdPoll.add("+,U<0$JS9S");
        listaSoloIdPoll.add("8GCO/W(VHU");
        listaSoloIdPoll.add("64J%@:>=P.");
        listaSoloIdPoll.add("B,VS.K=D%?");
        listaSoloIdPoll.add("SCI2LCI<X<");
        listaSoloIdPoll.add("#5$Q-@5X>P");
        listaSoloIdPoll.add("60JD/035YD");
        listaSoloIdPoll.add("C3X2:G9;WU");
        listaSoloIdPoll.add("N5D>JPPJ-:");
        listaSoloIdPoll.add("E@GOUFO3,)");
        listaSoloIdPoll.add("9ED5BM#&(T");
        listaSoloIdPoll.add("I$+I;A$,*G");
        listaSoloIdPoll.add("89R<1YUFZ1");
        listaSoloIdPoll.add("%9KA/5A4CP");
        listaSoloIdPoll.add("CP29K.>R-:");
        listaSoloIdPoll.add("*)7?6F8DVJ");
        listaSoloIdPoll.add("NY%+8FSDEG");
        listaSoloIdPoll.add("'#OKR2P25D");
        listaSoloIdPoll.add(".XDLWEQCE3");
        listaSoloIdPoll.add("7YGB*,=H<&");
        listaSoloIdPoll.add("2/FVOJFP$R");
        listaSoloIdPoll.add(".1/>U*R@GP");
        listaSoloIdPoll.add("6=S9SBAY3@");
        listaSoloIdPoll.add("'80J@N'LL'");
        listaSoloIdPoll.add("DGY:OD$=FV");
        listaSoloIdPoll.add("9L'E:8V:,<");
        listaSoloIdPoll.add("Q8P=2#MC$N");
        listaSoloIdPoll.add("Y4><3PN%@S");
        listaSoloIdPoll.add("PZTT$U&AB6");
        listaSoloIdPoll.add("B%1V+4NDS;");
        listaSoloIdPoll.add("N0L-$I5=@5");
        listaSoloIdPoll.add("BVG4,U?D+8");
        listaSoloIdPoll.add("KW6>,5,JZ,");
        listaSoloIdPoll.add("B2>Y??<5<W");
        listaSoloIdPoll.add("%%+$4$WB?R");
        listaSoloIdPoll.add("K1C%TA+(VI");
        listaSoloIdPoll.add("U4*,PC3/P7");
        listaSoloIdPoll.add(":*$@ATQI:V");
        listaSoloIdPoll.add("('BH.%)':O");
        listaSoloIdPoll.add("A'Z#XLAX31");
        listaSoloIdPoll.add("<@#I&-WEQ*");
        listaSoloIdPoll.add("N@Q.Y;X?E>");
        listaSoloIdPoll.add("%?Z+L*52;D");
        listaSoloIdPoll.add(",=;N=O0PB$");
        listaSoloIdPoll.add("#B1OHOR-TW");
        listaSoloIdPoll.add("L)N9>NLJ00");
        listaSoloIdPoll.add("UV59T*Z#X&");
        listaSoloIdPoll.add("?7%M>ELT,1");
        listaSoloIdPoll.add("?FI1RU#C14");
        listaSoloIdPoll.add("DU+9L9F+=N");
        listaSoloIdPoll.add("HZ33'//TD;");
        listaSoloIdPoll.add("@S8-SF(6X*");
        listaSoloIdPoll.add("2VY2;<E&N%");
        listaSoloIdPoll.add("9Z0K.:Y&IH");
        listaSoloIdPoll.add("W&JZ>0;F8@");
        listaSoloIdPoll.add("@4N;UE%W0-");
        listaSoloIdPoll.add("%O#I<%=9RM");
        listaSoloIdPoll.add("CF@@C)RLQ%");
        listaSoloIdPoll.add("F%%--/3:#M");
        listaSoloIdPoll.add("(R0YC@P4=(");
        listaSoloIdPoll.add("7$CC:)XA1L");
        listaSoloIdPoll.add("EZ-N.?.)P8");
        listaSoloIdPoll.add("Y),B?I>@T,");
        listaSoloIdPoll.add("?<X>T/FEY0");
        listaSoloIdPoll.add("@I&EYGD#'8");
        listaSoloIdPoll.add("$,#G'6-U7H");
        listaSoloIdPoll.add("OT;VB?XV5&");
        listaSoloIdPoll.add("Q(PU5##3F$");
        listaSoloIdPoll.add("8>GD#-K6MZ");
        listaSoloIdPoll.add(">7TQX4<:5S");
        listaSoloIdPoll.add("COE;EX/:L>");
        listaSoloIdPoll.add("GBIH9MPOV-");
        listaSoloIdPoll.add("D)=&NVEJR=");
        listaSoloIdPoll.add("63?V1OF=ZT");
        listaSoloIdPoll.add("X;>C.RW1M9");
        listaSoloIdPoll.add("LD=18QR<V;");

        return listaSoloIdPoll;
    }

}

/** Clase para gestionr los DUPLICADOS tanto de idPOLLS como de Cedulas **/
class GestionDuplicados{

    //Método para duplicados de IdPoll
    public List<String> metDuplicadosIdPolls(){
        List<String> listaPolls = new ArrayList<>();

        return listaPolls;
    }

    //Metodo para duplicados de cedulas
    public List<String> metDuplicadosCedulas(){
        List<String> listaPollsCedulas = new ArrayList<>();

        listaPollsCedulas.add("1006879150");
        listaPollsCedulas.add("1007209067");
        listaPollsCedulas.add("1007419315");
        listaPollsCedulas.add("1007616652");
        listaPollsCedulas.add("1010095335");
        listaPollsCedulas.add("1015444237");
        listaPollsCedulas.add("1023872275");
        listaPollsCedulas.add("1029604953");
        listaPollsCedulas.add("1029623790");
        listaPollsCedulas.add("10303634");
        listaPollsCedulas.add("1033775294");
        listaPollsCedulas.add("10389086");
        listaPollsCedulas.add("10483221");
        listaPollsCedulas.add("10490990");
        listaPollsCedulas.add("10491525");
        listaPollsCedulas.add("10495284");
        listaPollsCedulas.add("10526180");
        listaPollsCedulas.add("10534157");
        listaPollsCedulas.add("10543877");
        listaPollsCedulas.add("10551932");
        listaPollsCedulas.add("1058932334");
        listaPollsCedulas.add("1058935903");
        listaPollsCedulas.add("10589661");
        listaPollsCedulas.add("1058968238");
        listaPollsCedulas.add("105897055");
        listaPollsCedulas.add("1058971714");
        listaPollsCedulas.add("1058974674");
        listaPollsCedulas.add("1059047040");
        listaPollsCedulas.add("1059166657");
        listaPollsCedulas.add("1059236614");
        listaPollsCedulas.add("1059240003");
        listaPollsCedulas.add("105924392");
        listaPollsCedulas.add("1059360213");
        listaPollsCedulas.add("1059360859");
        listaPollsCedulas.add("1059363862");
        listaPollsCedulas.add("1059444666");
        listaPollsCedulas.add("1059445432");
        listaPollsCedulas.add("1059446402");
        listaPollsCedulas.add("1059447061");
        listaPollsCedulas.add("105944744");
        listaPollsCedulas.add("1059449162");
        listaPollsCedulas.add("1059449264");
        listaPollsCedulas.add("1059595490");
        listaPollsCedulas.add("1059597101");
        listaPollsCedulas.add("1059599451");
        listaPollsCedulas.add("1059601734");
        listaPollsCedulas.add("1059601877");
        listaPollsCedulas.add("1059602891");
        listaPollsCedulas.add("1059602906");
        listaPollsCedulas.add("1059604117");
        listaPollsCedulas.add("1059604439");
        listaPollsCedulas.add("1059605535");
        listaPollsCedulas.add("1059843889");
        listaPollsCedulas.add("1059844988");
        listaPollsCedulas.add("1059845804");
        listaPollsCedulas.add("105990072");
        listaPollsCedulas.add("1059901421");
        listaPollsCedulas.add("1059906299");
        listaPollsCedulas.add("105990736");
        listaPollsCedulas.add("1059912558");
        listaPollsCedulas.add("105991265");
        listaPollsCedulas.add("1059913053");
        listaPollsCedulas.add("105993798");
        listaPollsCedulas.add("1059986722");
        listaPollsCedulas.add("1060207660");
        listaPollsCedulas.add("1060208045");
        listaPollsCedulas.add("1060208549");
        listaPollsCedulas.add("1060236096");
        listaPollsCedulas.add("1060336544");
        listaPollsCedulas.add("1060363230");
        listaPollsCedulas.add("1060386498");
        listaPollsCedulas.add("1060467667");
        listaPollsCedulas.add("1060879812");
        listaPollsCedulas.add("1060988975");
        listaPollsCedulas.add("106107740");
        listaPollsCedulas.add("1061083377");
        listaPollsCedulas.add("1061198580");
        listaPollsCedulas.add("1061200886");
        listaPollsCedulas.add("1061210416");
        listaPollsCedulas.add("1061212148");
        listaPollsCedulas.add("1061215187");
        listaPollsCedulas.add("1061220985");
        listaPollsCedulas.add("106122191");
        listaPollsCedulas.add("1061222296");
        listaPollsCedulas.add("1061431524");
        listaPollsCedulas.add("1061432002");
        listaPollsCedulas.add("1061432788");
        listaPollsCedulas.add("1061434062");
        listaPollsCedulas.add("10614341129");
        listaPollsCedulas.add("1061434657");
        listaPollsCedulas.add("1061434773");
        listaPollsCedulas.add("1061436319");
        listaPollsCedulas.add("1061437615");
        listaPollsCedulas.add("1061440188");
        listaPollsCedulas.add("1061440263");
        listaPollsCedulas.add("1061440265");
        listaPollsCedulas.add("1061502799");
        listaPollsCedulas.add("1061503941");
        listaPollsCedulas.add("1061534824");
        listaPollsCedulas.add("1061538420");
        listaPollsCedulas.add("1061540762");
        listaPollsCedulas.add("1061541021");
        listaPollsCedulas.add("1061541108");
        listaPollsCedulas.add("1061542115");
        listaPollsCedulas.add("1061543046");
        listaPollsCedulas.add("1061543631");
        listaPollsCedulas.add("1061543750");
        listaPollsCedulas.add("1061543779");
        listaPollsCedulas.add("1061599464");
        listaPollsCedulas.add("1061600819");
        listaPollsCedulas.add("1061687962");
        listaPollsCedulas.add("1061689653");
        listaPollsCedulas.add("1061692452");
        listaPollsCedulas.add("1061696754");
        listaPollsCedulas.add("1061712555");
        listaPollsCedulas.add("1061712941");
        listaPollsCedulas.add("1061717338");
        listaPollsCedulas.add("1061726472");
        listaPollsCedulas.add("1061748602");
        listaPollsCedulas.add("106174939");
        listaPollsCedulas.add("1061749687");
        listaPollsCedulas.add("1061750417");
        listaPollsCedulas.add("1061757008");
        listaPollsCedulas.add("106176165");
        listaPollsCedulas.add("1061787029");
        listaPollsCedulas.add("1061790582");
        listaPollsCedulas.add("1061794482");
        listaPollsCedulas.add("10620078727");
        listaPollsCedulas.add("1062014170");
        listaPollsCedulas.add("1062074294");
        listaPollsCedulas.add("1062077228");
        listaPollsCedulas.add("1062078033");
        listaPollsCedulas.add("1062078183");
        listaPollsCedulas.add("1062078625");
        listaPollsCedulas.add("106207917");
        listaPollsCedulas.add("1062079304");
        listaPollsCedulas.add("1062079334");
        listaPollsCedulas.add("1062079870");
        listaPollsCedulas.add("1062080432");
        listaPollsCedulas.add("1062080997");
        listaPollsCedulas.add("1062081038");
        listaPollsCedulas.add("1062082537");
        listaPollsCedulas.add("1062083350");
        listaPollsCedulas.add("1062083419");
        listaPollsCedulas.add("1062083758");
        listaPollsCedulas.add("1062083822");
        listaPollsCedulas.add("1062084595");
        listaPollsCedulas.add("1062084686");
        listaPollsCedulas.add("1062085034");
        listaPollsCedulas.add("1062085217");
        listaPollsCedulas.add("1062085775");
        listaPollsCedulas.add("1062085898");
        listaPollsCedulas.add("106208849");
        listaPollsCedulas.add("106213415");
        listaPollsCedulas.add("1062219520");
        listaPollsCedulas.add("10622279860");
        listaPollsCedulas.add("1062278837");
        listaPollsCedulas.add("1062279398");
        listaPollsCedulas.add("1062300671");
        listaPollsCedulas.add("1062307714");
        listaPollsCedulas.add("1062313123");
        listaPollsCedulas.add("1062317907");
        listaPollsCedulas.add("1062318434");
        listaPollsCedulas.add("1062319589");
        listaPollsCedulas.add("1062324318");
        listaPollsCedulas.add("1062334023");
        listaPollsCedulas.add("1062334178");
        listaPollsCedulas.add("1062335577");
        listaPollsCedulas.add("1062335786");
        listaPollsCedulas.add("1062536387");
        listaPollsCedulas.add("1062774328");
        listaPollsCedulas.add("1062775152");
        listaPollsCedulas.add("1062778257");
        listaPollsCedulas.add("1062778429");
        listaPollsCedulas.add("1062780211");
        listaPollsCedulas.add("10633612");
        listaPollsCedulas.add("1063434835");
        listaPollsCedulas.add("1063809267");
        listaPollsCedulas.add("1063812720");
        listaPollsCedulas.add("1063814119");
        listaPollsCedulas.add("1064430880");
        listaPollsCedulas.add("1066846338");
        listaPollsCedulas.add("1067462876");
        listaPollsCedulas.add("1067526995");
        listaPollsCedulas.add("1068217139");
        listaPollsCedulas.add("1068218002");
        listaPollsCedulas.add("10690652");
        listaPollsCedulas.add("10752288");
        listaPollsCedulas.add("10752703");
        listaPollsCedulas.add("1075301335");
        listaPollsCedulas.add("1081280156");
        listaPollsCedulas.add("1082776915");
        listaPollsCedulas.add("1084576099");
        listaPollsCedulas.add("1084867461");
        listaPollsCedulas.add("10852755585");
        listaPollsCedulas.add("1086358249");
        listaPollsCedulas.add("1087202430");
        listaPollsCedulas.add("1106363440");
        listaPollsCedulas.add("110783904");
        listaPollsCedulas.add("1109119532");
        listaPollsCedulas.add("110967310");
        listaPollsCedulas.add("1110060113");
        listaPollsCedulas.add("1111111");
        listaPollsCedulas.add("11111111");
        listaPollsCedulas.add("111154227");
        listaPollsCedulas.add("1112061053");
        listaPollsCedulas.add("1112465426");
        listaPollsCedulas.add("1114061364");
        listaPollsCedulas.add("1114874162");
        listaPollsCedulas.add("1114888718");
        listaPollsCedulas.add("1114894135");
        listaPollsCedulas.add("1115791763");
        listaPollsCedulas.add("1116206758");
        listaPollsCedulas.add("1116232712");
        listaPollsCedulas.add("1116377621");
        listaPollsCedulas.add("1117496631");
        listaPollsCedulas.add("1117501891");
        listaPollsCedulas.add("1117514603");
        listaPollsCedulas.add("1118202284");
        listaPollsCedulas.add("1118473350");
        listaPollsCedulas.add("1120066482");
        listaPollsCedulas.add("1127076145");
        listaPollsCedulas.add("1127360480");
        listaPollsCedulas.add("1127672382");
        listaPollsCedulas.add("113102405");
        listaPollsCedulas.add("113102415");
        listaPollsCedulas.add("113102422");
        listaPollsCedulas.add("113102431");
        listaPollsCedulas.add("113102436");
        listaPollsCedulas.add("113102439");
        listaPollsCedulas.add("113102442");
        listaPollsCedulas.add("113102447");
        listaPollsCedulas.add("113102448");
        listaPollsCedulas.add("113983405");
        listaPollsCedulas.add("1143956289");
        listaPollsCedulas.add("114452413");
        listaPollsCedulas.add("114452419");
        listaPollsCedulas.add("1144524321");
        listaPollsCedulas.add("1144524412");
        listaPollsCedulas.add("114452499");
        listaPollsCedulas.add("114452501");
        listaPollsCedulas.add("114452529");
        listaPollsCedulas.add("114452540");
        listaPollsCedulas.add("1149685378");
        listaPollsCedulas.add("1149686579");
        listaPollsCedulas.add("1149686722");
        listaPollsCedulas.add("1149688407");
        listaPollsCedulas.add("1193076027");
        listaPollsCedulas.add("119336502");
        listaPollsCedulas.add("1194087240");
        listaPollsCedulas.add("1219996");
        listaPollsCedulas.add("12283096");
        listaPollsCedulas.add("14479016");
        listaPollsCedulas.add("14622481");
        listaPollsCedulas.add("16786481");
        listaPollsCedulas.add("16892279");
        listaPollsCedulas.add("20838274");
        listaPollsCedulas.add("24622141");
        listaPollsCedulas.add("25255647");
        listaPollsCedulas.add("25267406");
        listaPollsCedulas.add("25281327");
        listaPollsCedulas.add("25288035");
        listaPollsCedulas.add("25292284");
        listaPollsCedulas.add("25296164");
        listaPollsCedulas.add("25310738");
        listaPollsCedulas.add("25347890");
        listaPollsCedulas.add("25354233");
        listaPollsCedulas.add("25359275");
        listaPollsCedulas.add("25362826");
        listaPollsCedulas.add("25363118");
        listaPollsCedulas.add("25363444");
        listaPollsCedulas.add("25364069");
        listaPollsCedulas.add("25364718");
        listaPollsCedulas.add("253650683");
        listaPollsCedulas.add("25366344");
        listaPollsCedulas.add("25371314");
        listaPollsCedulas.add("25386768");
        listaPollsCedulas.add("25390086");
        listaPollsCedulas.add("25394689");
        listaPollsCedulas.add("25395314");
        listaPollsCedulas.add("25483067");
        listaPollsCedulas.add("25517993");
        listaPollsCedulas.add("25521509");
        listaPollsCedulas.add("25527607");
        listaPollsCedulas.add("25543806");
        listaPollsCedulas.add("25544480");
        listaPollsCedulas.add("25558746");
        listaPollsCedulas.add("25559022");
        listaPollsCedulas.add("25560525");
        listaPollsCedulas.add("25561333");
        listaPollsCedulas.add("25561400");
        listaPollsCedulas.add("25564364");
        listaPollsCedulas.add("25564703");
        listaPollsCedulas.add("25576779");
        listaPollsCedulas.add("25578868");
        listaPollsCedulas.add("25592676");
        listaPollsCedulas.add("25602452");
        listaPollsCedulas.add("25608432");
        listaPollsCedulas.add("25659323");
        listaPollsCedulas.add("25669522");
        listaPollsCedulas.add("25717723");
        listaPollsCedulas.add("25733161");
        listaPollsCedulas.add("25734407");
        listaPollsCedulas.add("25734655");
        listaPollsCedulas.add("25741780");
        listaPollsCedulas.add("25741813");
        listaPollsCedulas.add("25742232");
        listaPollsCedulas.add("27260453");
        listaPollsCedulas.add("27371394");
        listaPollsCedulas.add("2869878");
        listaPollsCedulas.add("29179472");
        listaPollsCedulas.add("29360767");
        listaPollsCedulas.add("29507868");
        listaPollsCedulas.add("29509548");
        listaPollsCedulas.add("29510532");
        listaPollsCedulas.add("29561320");
        listaPollsCedulas.add("29583629");
        listaPollsCedulas.add("30520251");
        listaPollsCedulas.add("31475978");
        listaPollsCedulas.add("31626901");
        listaPollsCedulas.add("31847382");
        listaPollsCedulas.add("31885495");
        listaPollsCedulas.add("34374991");
        listaPollsCedulas.add("34380235");
        listaPollsCedulas.add("34445957");
        listaPollsCedulas.add("34501610");
        listaPollsCedulas.add("34509572");
        listaPollsCedulas.add("34516176");
        listaPollsCedulas.add("34524132");
        listaPollsCedulas.add("34525839");
        listaPollsCedulas.add("34526052");
        listaPollsCedulas.add("34530196");
        listaPollsCedulas.add("34544241");
        listaPollsCedulas.add("34546752");
        listaPollsCedulas.add("34547235");
        listaPollsCedulas.add("34557660");
        listaPollsCedulas.add("34558016");
        listaPollsCedulas.add("34559811");
        listaPollsCedulas.add("34560193");
        listaPollsCedulas.add("34565063");
        listaPollsCedulas.add("34571163");
        listaPollsCedulas.add("34599364");
        listaPollsCedulas.add("34602217");
        listaPollsCedulas.add("34605571");
        listaPollsCedulas.add("34610401");
        listaPollsCedulas.add("34612196");
        listaPollsCedulas.add("34615003");
        listaPollsCedulas.add("34638761");
        listaPollsCedulas.add("34674537");
        listaPollsCedulas.add("34678677");
        listaPollsCedulas.add("34680075");
        listaPollsCedulas.add("34700187");
        listaPollsCedulas.add("38561289");
        listaPollsCedulas.add("40081034");
        listaPollsCedulas.add("403055563");
        listaPollsCedulas.add("40621552");
        listaPollsCedulas.add("40740367");
        listaPollsCedulas.add("4609760");
        listaPollsCedulas.add("4609919");
        listaPollsCedulas.add("4619811");
        listaPollsCedulas.add("4620358");
        listaPollsCedulas.add("4681151");
        listaPollsCedulas.add("4686163");
        listaPollsCedulas.add("4692025");
        listaPollsCedulas.add("4722486");
        listaPollsCedulas.add("47296966");
        listaPollsCedulas.add("4742210");
        listaPollsCedulas.add("4742842");
        listaPollsCedulas.add("4745576");
        listaPollsCedulas.add("4751052");
        listaPollsCedulas.add("4784077");
        listaPollsCedulas.add("4787459");
        listaPollsCedulas.add("4788137");
        listaPollsCedulas.add("48576199");
        listaPollsCedulas.add("48608803");
        listaPollsCedulas.add("48615063");
        listaPollsCedulas.add("48629564");
        listaPollsCedulas.add("48648961");
        listaPollsCedulas.add("48658046");
        listaPollsCedulas.add("48660128");
        listaPollsCedulas.add("48668115");
        listaPollsCedulas.add("48680176");
        listaPollsCedulas.add("48680321");
        listaPollsCedulas.add("5273935");
        listaPollsCedulas.add("59793457");
        listaPollsCedulas.add("6085249");
        listaPollsCedulas.add("6393656");
        listaPollsCedulas.add("6431480");
        listaPollsCedulas.add("66705111");
        listaPollsCedulas.add("67021841");
        listaPollsCedulas.add("69055277");
        listaPollsCedulas.add("76003110");
        listaPollsCedulas.add("760044070");
        listaPollsCedulas.add("76006086");
        listaPollsCedulas.add("76006667");
        listaPollsCedulas.add("76009684");
        listaPollsCedulas.add("76141667");
        listaPollsCedulas.add("76143074");
        listaPollsCedulas.add("76143138");
        listaPollsCedulas.add("76215383");
        listaPollsCedulas.add("76259224");
        listaPollsCedulas.add("76268520");
        listaPollsCedulas.add("76279880");
        listaPollsCedulas.add("76285444");
        listaPollsCedulas.add("76305604");
        listaPollsCedulas.add("76308235");
        listaPollsCedulas.add("76319421");
        listaPollsCedulas.add("76335763");
        listaPollsCedulas.add("76350920");
        listaPollsCedulas.add("8402200808");
        listaPollsCedulas.add("87712347");
        listaPollsCedulas.add("96355120");

        return listaPollsCedulas;

    }
}

/** Para evaluar las otras tablas */
class procesaridPollAndFechas{
    //    procesaridPollAndFechas.add(new procesaridPollAndFechas("hola1","idiii1"));
    public String createdAt;
    public String idPoll;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(String idPoll) {
        this.idPoll = idPoll;
    }

    public procesaridPollAndFechas(String createdAt, String idPoll) {
        this.createdAt = createdAt;
        this.idPoll = idPoll;
    }

    public procesaridPollAndFechas() {
    }

}