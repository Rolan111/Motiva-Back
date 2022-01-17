package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.payload.RepComAgentForm;
import co.edu.ucc.motivaback.service.RepComAgentService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class RepComAgentServicetImpl implements RepComAgentService {

    private final FirebaseInitializer firebase;
    private final ModelMapper modelMapper;

    public RepComAgentServicetImpl(FirebaseInitializer firebase, ModelMapper modelMapper) {
        this.firebase = firebase;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RepComAgentDto> findAll() {
        List<RepComAgentDto> response = new ArrayList<>();
        String id1 = "hola";
        //ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            //for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var repComAgentDto = doc.toObject(RepComAgentDto.class);
                repComAgentDto.setDocumentRepComAgentId(doc.getId()); //SE DEBE AGREGAR UN ID AL BEAN?
                response.add(repComAgentDto);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public RepComAgentDto findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference ref = getCollection().document(id);
        ApiFuture<DocumentSnapshot> futureDoc = ref.get();
        DocumentSnapshot document = futureDoc.get();

        var repComAgentDto = document.toObject(RepComAgentDto.class);
        return repComAgentDto;
    }

    @Override
    public RepComAgentDto create(RepComAgentForm repComAgentForm) {

        //RepComAgentForm answer = new RepComAgentForm();
/* DATOS DE PRUEBA
        answer.setActivityName("Actividad 1");
        answer.setActivityNumber("1");
        answer.setDate("2014-04-21");
        answer.setDuration("2 horas");
        answer.setPlace("Popayán");
        answer.setNumberAttendees("4");
        answer.setActivityObjectives("Descripción de objetivos");
        answer.setResourcesUsed("Descripción recursos usados");
        answer.setMethodologyUsed("Descripción metodología usada");
        answer.setActivityDescriptionDevelopment("Descripción y desarrollo de la actividad");
        answer.setResourcesObtained("Recursos obtenidos");
        answer.setActivityProfessionalincharge("Profesional encargado de esta actividad");
*/
        Map<String, Object> docData = getDocData(repComAgentForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
        var repComAgentDto = new RepComAgentDto();
        try {
            if (writeResultApiFuture.get() != null)
                repComAgentDto = modelMapper.map(repComAgentForm, RepComAgentDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return repComAgentDto;
    }

    @Override
    public RepComAgentDto update(RepComAgentForm repComAgentForm) {
        Map<String, Object> docData = getDocData(repComAgentForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(repComAgentForm.getDocumentRepComAgentId()).set(docData);
        var repComAgentDto = new RepComAgentDto();

        try {
            if (writeResultApiFuture.get() != null)
                repComAgentDto = modelMapper.map(repComAgentForm, RepComAgentDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return repComAgentDto;
    }

    @Override
    public boolean delete(String id) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();

            return writeResultApiFuture.get() != null;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }


    /*@Override
    public RepComAgentDto findById(String id) {
        return null;
    }*/

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("rep-com-agent");
    }

    private Map<String, Object> getDocData(RepComAgentForm answer) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("activityName", answer.getActivityName());
        docData.put("activityNumber", answer.getActivityNumber());
        docData.put("date", answer.getDate());
        docData.put("duration", answer.getDuration());
        docData.put("place", answer.getPlace());
        docData.put("numberAttendees", answer.getNumberAttendees());
        docData.put("activityObjectives", answer.getActivityObjectives());
        docData.put("resourcesUsed", answer.getResourcesUsed());
        docData.put("methodologyUsed", answer.getMethodologyUsed());
        docData.put("activityDescriptionDevelopment", answer.getActivityDescriptionDevelopment());
        docData.put("resourcesObtained", answer.getResourcesObtained());
        docData.put("activityProfessionalincharge", answer.getActivityProfessionalincharge());
        return docData;
    }
}
