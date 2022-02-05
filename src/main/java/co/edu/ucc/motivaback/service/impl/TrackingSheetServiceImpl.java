package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.service.TrackingSheetService;
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
public class TrackingSheetServiceImpl implements TrackingSheetService {
    private final FirebaseInitializer firebase;
    private final CollectionReference collectionReference;

    public TrackingSheetServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
        this.collectionReference = getFirebaseCollection(this.firebase, CommonsService.COLLECTION_NAME_TRACKING_SHEET);
    }

    private TrackingSheetDto getTrackingSheetDto(DocumentSnapshot doc) {
        var jsonString = getGson().toJson(doc.getData());
        TrackingSheetDto trackingSheetDto = getGson().fromJson(jsonString, TrackingSheetDto.class);

        trackingSheetDto.setId(doc.getId());
        return trackingSheetDto;
    }

    @Override
    public List<TrackingSheetDto> findAll() {
        try {
            return this.collectionReference.get().get().getDocuments().stream().map(this::getTrackingSheetDto)
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public TrackingSheetDto findById(String id) {
        try {
            return getTrackingSheetDto(this.collectionReference.document(id).get().get());
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public TrackingSheetDto create(TrackingSheetDto inTrackingSheetDto) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = this.collectionReference.document().create(getDocData(inTrackingSheetDto));

            if (writeResultApiFuture.get() != null)
                return inTrackingSheetDto;
            else
                return null;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public TrackingSheetDto update(TrackingSheetDto trackingSheetDto) {
        try {
            ApiFuture<WriteResult> writeResultApiFuture = this.collectionReference.document(trackingSheetDto.getId()).set(getDocData(trackingSheetDto));

            if (writeResultApiFuture.get() != null)
                return trackingSheetDto;
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

    private Map<String, Object> getDocData(TrackingSheetDto trackingSheetDto) {
        Map<String, Object> docData = new HashMap<>();

        docData.put(NAMES, trackingSheetDto.getNames());
        docData.put(LASTNAMES, trackingSheetDto.getLastnames());
        docData.put(TYPE, trackingSheetDto.getType());
        docData.put(IDENTIFICATION, trackingSheetDto.getIdentification());
        docData.put(TYPE_ROUTE, trackingSheetDto.getTypeRoute());
        docData.put(REFERRED_ENTITY, trackingSheetDto.getReferredEntity());
        docData.put(ATTENTION_STATUS, trackingSheetDto.getAttentionStatus());
        docData.put(RECOMMENDATIONS, trackingSheetDto.getRecommendations());
        return docData;
    }
}
