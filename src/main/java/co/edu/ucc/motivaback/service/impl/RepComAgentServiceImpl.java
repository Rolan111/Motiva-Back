package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.service.RepComAgentService;
import co.edu.ucc.motivaback.util.CommonsService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@Service
public class RepComAgentServiceImpl implements RepComAgentService {

    private final FirebaseInitializer firebase;
    private final CollectionReference collectionReference;

    public RepComAgentServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
        this.collectionReference = getFirebaseCollection(this.firebase, CommonsService.COLLECTION_NAME_REP_COM_AGENT);
    }

    private RepComAgentDto getRepComAgentDto(DocumentSnapshot doc) {
        var jsonString = getGson().toJson(doc.getData());
        RepComAgentDto repComAgentDto = getGson().fromJson(jsonString, RepComAgentDto.class);

        repComAgentDto.setId(doc.getId());
        return repComAgentDto;
    }

    @Override
    public List<RepComAgentDto> findAll() {
        try {
            return this.collectionReference.get().get().getDocuments().stream().map(this::getRepComAgentDto)
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public RepComAgentDto findById(String id) {
        try {
            return getRepComAgentDto(this.collectionReference.document(id).get().get());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public RepComAgentDto create(RepComAgentDto inRepComAgentDto) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = this.collectionReference.document().create(getDocData(inRepComAgentDto));

            if (writeResultApiFuture.get() != null)
                return inRepComAgentDto;
            else
                return null;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public RepComAgentDto update(RepComAgentDto repComAgentDto) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = this.collectionReference.document(repComAgentDto.getId()).set(getDocData(repComAgentDto));

            if (writeResultApiFuture.get() != null)
                return repComAgentDto;
            else
                return null;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = this.collectionReference.document(id).delete();

            return writeResultApiFuture.get() != null;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private Map<String, Object> getDocData(RepComAgentDto repComAgentDto) {
        Map<String, Object> docData = new HashMap<>();

        docData.put(ACTIVITY_NAME, repComAgentDto.getActivityName());
        docData.put(ACTIVITY_NUMBER, repComAgentDto.getActivityNumber());
        docData.put(DATE, repComAgentDto.getDate());
        docData.put(DURATION, repComAgentDto.getDuration());
        docData.put(PLACE, repComAgentDto.getPlace());
        docData.put(NUMBER_ATTENDEES, repComAgentDto.getNumberAttendees());
        docData.put(ACTIVITY_OBJECTIVES, repComAgentDto.getActivityObjectives());
        docData.put(RESOURCES_USED, repComAgentDto.getResourcesUsed());
        docData.put(METHODOLOGY_USED, repComAgentDto.getMethodologyUsed());
        docData.put(ACTIVITY_DESCRIPTION_DEVELOPMENT, repComAgentDto.getActivityDescriptionDevelopment());
        docData.put(RESOURCES_OBTAINED, repComAgentDto.getResourcesObtained());
        docData.put(EVIDENCE, repComAgentDto.getEvidence());
        docData.put(ACTIVITY_PROFESSIONAL_INCHARGE, repComAgentDto.getActivityProfessionalIncharge());
        return docData;
    }
}
