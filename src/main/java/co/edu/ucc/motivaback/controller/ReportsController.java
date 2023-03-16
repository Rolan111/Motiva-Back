package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.entity.AnswerEntity;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ReportsController {

    @GetMapping(value = "/surveysByMunicipality/{municipality}")
    public int surveysByMunicipality(@PathVariable String municipality) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");

        Query query = documentReference.whereEqualTo("id_question", 6).whereEqualTo("open_answer", municipality); //para adulto
        Query query2 = documentReference.whereEqualTo("id_question", 5).whereEqualTo("open_answer", municipality); //para NIÑOS
        ApiFuture<QuerySnapshot> future = query.get();
        ApiFuture<QuerySnapshot> future2 = query2.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<QueryDocumentSnapshot> documents2 = future2.get().getDocuments();


        System.out.println("La cantidad de datos ADULTOS es: "+documents.size());
        System.out.println("La cantidad de datos NIÑOS es: "+documents2.size());

        int totalNinoAdulto=documents.size()+documents2.size();

//        for (QueryDocumentSnapshot doc : documents){
//            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
//            commentsEntities.add(commentsDto);
//        }
        return totalNinoAdulto;
    }

    @GetMapping(value = "/surveysByProfessional/{idProfessional}")
    public int surveysByProfessional(@PathVariable int idProfessional) throws ExecutionException, InterruptedException {

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");

        Query query = documentReference.whereEqualTo("id_user", idProfessional); //para adulto
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        System.out.println("La cantidad de encuestas de este PROFESIONAL es: "+documents.size());

        return documents.size();
    }

    @GetMapping(value = "/idPollsBasedData/{fechaInicio}/{fechaFin}")
    public void IdPollsEnbaseUnaFecha(@PathVariable String fechaInicio, @PathVariable String fechaFin) throws ExecutionException, InterruptedException {
        /** Este servicio espera el formato en string: 2022-12-01T00:00:00   ---  Luego se transforma en Timestamp **/
        //Instant maneja el formato en que se guarda en Firebase, pero para CONSULTAS funciona mejor "Timestamp"
        Timestamp fechafireInicio = Timestamp.parseTimestamp(fechaInicio);
        Timestamp fechafireFin = Timestamp.parseTimestamp(fechaFin);

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereGreaterThanOrEqualTo("created_at", fechafireInicio).whereLessThanOrEqualTo("created_at", fechafireFin);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        System.out.println("La cantidad de datos es: "+documents.size());
        for (QueryDocumentSnapshot doc : documents){
            System.out.println("Los datos son: "+doc.getData().get("created_at"));
        }
    }
}
