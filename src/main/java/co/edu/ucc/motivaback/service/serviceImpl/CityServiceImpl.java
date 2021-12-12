package co.edu.ucc.motivaback.service.serviceImpl;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        CityDto cityDto;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                cityDto = doc.toObject(CityDto.class);
                cityDto.setDocumentCityId(doc.getId());
                response.add(cityDto);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CityDto create(CityForm cityForm) {
        Map<String, Object> docData = getDocData(cityForm);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(cityForm, CityDto.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CityDto update(CityForm cityForm) {
        Map<String, Object> docData = getDocData(cityForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(cityForm.getDocumentCityId()).set(docData);
        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(cityForm, CityDto.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
        try {
            if (writeResultApiFuture.get() != null) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
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
