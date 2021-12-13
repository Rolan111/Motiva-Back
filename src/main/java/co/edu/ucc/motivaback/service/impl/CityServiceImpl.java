package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.CityDto;
import co.edu.ucc.motivaback.payload.CityForm;
import co.edu.ucc.motivaback.service.CityService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author nagredo
 * @project motiva-back
 * @class CityServiceImpl
 */
@Service
public class CityServiceImpl implements CityService {

    private final FirebaseInitializer firebase;
    private final ModelMapper modelMapper;

    public CityServiceImpl(FirebaseInitializer firebase, ModelMapper modelMapper) {
        this.firebase = firebase;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CityDto> findAll() {
        List<CityDto> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var cityDto = doc.toObject(CityDto.class);
                cityDto.setDocumentCityId(doc.getId());
                response.add(cityDto);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public CityDto create(CityForm cityForm) {
        Map<String, Object> docData = getDocData(cityForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
        var cityDto = new CityDto();

        try {
            if (writeResultApiFuture.get() != null)
                cityDto = modelMapper.map(cityForm, CityDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return cityDto;
    }

    @Override
    public CityDto update(CityForm cityForm) {
        Map<String, Object> docData = getDocData(cityForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(cityForm.getDocumentCityId()).set(docData);
        var cityDto = new CityDto();

        try {
            if (writeResultApiFuture.get() != null)
                cityDto = modelMapper.map(cityForm, CityDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return cityDto;
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

    @Override
    public CityDto findById(String id) {
        return null;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("city");
    }

    private Map<String, Object> getDocData(CityForm cityForm) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("name", cityForm.getName());
        docData.put("count", cityForm.getCount());
        return docData;
    }
}
