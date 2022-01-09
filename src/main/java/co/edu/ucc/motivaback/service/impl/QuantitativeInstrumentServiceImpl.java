package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.QuantitativeInstrumentDto;
import co.edu.ucc.motivaback.payload.QuantitativeInstrumentForm;
import co.edu.ucc.motivaback.service.QuantitativeInstrumentService;
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
 * @class QuantitativeInstrumentServiceImpl
 */
@Service
public class QuantitativeInstrumentServiceImpl implements QuantitativeInstrumentService {

    private final FirebaseInitializer firebase;
    private final ModelMapper modelMapper;

    public QuantitativeInstrumentServiceImpl(FirebaseInitializer firebase, ModelMapper modelMapper) {
        this.firebase = firebase;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<QuantitativeInstrumentDto> findAll() {
        List<QuantitativeInstrumentDto> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var quantitativeInstrumentDto = doc.toObject(QuantitativeInstrumentDto.class);
                quantitativeInstrumentDto.setDocumentId(doc.getId());
                response.add(quantitativeInstrumentDto);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public QuantitativeInstrumentDto create(QuantitativeInstrumentForm quantitativeInstrumentForm) {
        Map<String, Object> docData = getDocData(quantitativeInstrumentForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
        var quantitativeInstrumentDto = new QuantitativeInstrumentDto();

        try {
            if (writeResultApiFuture.get() != null)
                quantitativeInstrumentDto = modelMapper.map(quantitativeInstrumentForm, QuantitativeInstrumentDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return quantitativeInstrumentDto;
    }

    @Override
    public QuantitativeInstrumentDto update(QuantitativeInstrumentForm quantitativeInstrumentForm) {
        Map<String, Object> docData = getDocData(quantitativeInstrumentForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(quantitativeInstrumentForm.getDocumentId()).set(docData);
        var quantitativeInstrumentDto = new QuantitativeInstrumentDto();

        try {
            if (writeResultApiFuture.get() != null)
                quantitativeInstrumentDto = modelMapper.map(quantitativeInstrumentForm, QuantitativeInstrumentDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return quantitativeInstrumentDto;
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
    public QuantitativeInstrumentDto findById(String id) {
        return null;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("city");
    }

    private Map<String, Object> getDocData(QuantitativeInstrumentForm quantitativeInstrumentForm) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("age", quantitativeInstrumentForm.getAge());
        docData.put("sex", quantitativeInstrumentForm.getSex());
        return docData;
    }
}
