package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.QuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;
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
    public QuantitativeInstrumentDto create(AnswerQuantitativeInstrumentForm answerQuantitativeInstrumentForm) {
        var quantitativeInstrumentDto = new QuantitativeInstrumentDto();

        Map<String, Object> docData = getDocData(answerQuantitativeInstrumentForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

        try {
            if (writeResultApiFuture.get() != null)
                quantitativeInstrumentDto = modelMapper.map(answerQuantitativeInstrumentForm, QuantitativeInstrumentDto.class);

        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return quantitativeInstrumentDto;
    }

    @Override
    public QuantitativeInstrumentDto update(QuantitativeInstrumentForm quantitativeInstrumentForm) {
        var quantitativeInstrumentDto = new QuantitativeInstrumentDto();
        AnswerQuantitativeInstrumentForm answer = new AnswerQuantitativeInstrumentForm();

        Map<String, Object> docData = getDocData(answer);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(quantitativeInstrumentForm.getDocumentId()).set(docData);

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

    @Override
    public List<QuestionDto> findAllQuestion() {
        List<QuestionDto> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollectionQuestion().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                var questionDto = doc.toObject(QuestionDto.class);
                response.add(questionDto);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("answer");
    }

    private CollectionReference getCollectionQuestion() {
        return firebase.getFirestore().collection("question");
    }

    private Map<String, Object> getDocData(AnswerQuantitativeInstrumentForm answer) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("id_answer", answer.getIdAnswer());
        docData.put("id_question", answer.getIdQuestion());
        docData.put("id_option_answer", answer.getIdOptionAnswer());
        docData.put("open_answer", answer.getOpenAnswer());
        docData.put("id_poll", answer.getIdPoll());
        return docData;
    }
}
