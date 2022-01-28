package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.payload.TrackingSheetForm;
import co.edu.ucc.motivaback.service.TrackingSheetService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class TrackingSheetServiceImpl implements TrackingSheetService {

    private final FirebaseInitializer firebase;
    private final ModelMapper modelMapper;


    public TrackingSheetServiceImpl(FirebaseInitializer firebase, ModelMapper modelMapper) {
        this.firebase = firebase;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TrackingSheetDto> findAll() {
        List<TrackingSheetDto> response = new ArrayList<>();
        String id1 = "hola";
        //ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            //for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var trackingSheetDto = doc.toObject(TrackingSheetDto.class);
                trackingSheetDto.setDocumentTrackingSheetId(doc.getId()); //SE DEBE AGREGAR UN ID AL BEAN?
                response.add(trackingSheetDto);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public TrackingSheetDto findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference ref = getCollection().document(id);
        ApiFuture<DocumentSnapshot> futureDoc = ref.get();
        DocumentSnapshot document = futureDoc.get();

        var trackingSheetDto = document.toObject(TrackingSheetDto.class);
        return trackingSheetDto;
    }

    @Override
    public TrackingSheetDto create(TrackingSheetForm trackingSheetForm) {
        Map<String, Object> docData = getDocData(trackingSheetForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
        var trackingSheetDto = new TrackingSheetDto();
        try {
            if (writeResultApiFuture.get() != null)
                trackingSheetDto = modelMapper.map(trackingSheetForm, TrackingSheetDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return trackingSheetDto;
    }

    @Override
    public TrackingSheetDto update(TrackingSheetForm trackingSheetForm) {
        Map<String, Object> docData = getDocData(trackingSheetForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(trackingSheetForm.getDocumentTrackingSheetId()).set(docData);
        var trackingSheetDto = new TrackingSheetDto();

        try {
            if (writeResultApiFuture.get() != null)
                trackingSheetDto = modelMapper.map(trackingSheetForm, TrackingSheetDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return trackingSheetDto;
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
        return firebase.getFirestore().collection("tracking_sheet");
    }

    private Map<String, Object> getDocData(TrackingSheetForm answer) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("names", answer.getNames());
        docData.put("lastnames", answer.getLastnames());
        docData.put("identification_type", answer.getIdentification_type());
        docData.put("n_identification", answer.getN_identification());
        docData.put("type_route", answer.getType_route());
        docData.put("referred_entity", answer.getReferred_entity());
        docData.put("attention_status", answer.getAttention_status());
        docData.put("recommendations_suggestions", answer.getRecommendations_suggestions());
        return docData;
    }
}
