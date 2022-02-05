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
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var repComAgentDto = doc.toObject(RepComAgentDto.class);
                repComAgentDto.setId(doc.getId());
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

        return document.toObject(RepComAgentDto.class);
    }

    @Override
    public RepComAgentDto create(RepComAgentForm repComAgentForm) {
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

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("rep_com_agent");
    }

    private Map<String, Object> getDocData(RepComAgentForm answer) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("activity_name", answer.getActivity_name());
        docData.put("activity_number", answer.getActivity_number());
        docData.put("date", answer.getDate());
        docData.put("duration", answer.getDuration());
        docData.put("place", answer.getPlace());
        docData.put("number_attendees", answer.getNumber_attendees());
        docData.put("activity_objectives", answer.getActivity_objectives());
        docData.put("resources_used", answer.getResources_used());
        docData.put("methodology_used", answer.getMethodology_used());
        docData.put("activity_description_development", answer.getActivity_description_development());
        docData.put("resources_obtained", answer.getResources_obtained());
        docData.put("evidence", answer.getEvidence());
        docData.put("activity_professional_incharge", answer.getActivity_professional_incharge());
        return docData;
    }
}
