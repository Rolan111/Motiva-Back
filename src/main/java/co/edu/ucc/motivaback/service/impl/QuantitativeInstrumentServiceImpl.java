package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;
import co.edu.ucc.motivaback.service.QuantitativeInstrumentService;
import co.edu.ucc.motivaback.util.CommonsService;
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
    public List<AnswerQuantitativeInstrumentDto> findAll() {
        List<AnswerQuantitativeInstrumentDto> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                AnswerQuantitativeInstrumentDto answer = new AnswerQuantitativeInstrumentDto();

                answer.setIdAnswer((Long) doc.getData().get(CommonsService.ID_ANSWER));
                answer.setIdQuestion((Long) doc.getData().get(CommonsService.ID_QUESTION));
                answer.setIdOptionAnswer((Long) doc.getData().get(CommonsService.ID_OPTION_ANSWER));
                answer.setOpenAnswer(doc.getData().get(CommonsService.OPEN_ANSWER).toString());
                answer.setIdPoll((Long) doc.getData().get(CommonsService.ID_POLL));

                response.add(answer);
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public AnswerQuantitativeInstrumentDto create(List<AnswerQuantitativeInstrumentForm> answerQuantitativeInstrumentForm) {
        var quantitativeInstrumentDto = new AnswerQuantitativeInstrumentDto();

        for (AnswerQuantitativeInstrumentForm quantitativeInstrumentForm : answerQuantitativeInstrumentForm) {
            Map<String, Object> docData = getDocData(quantitativeInstrumentForm);
            ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

            try {
                if (writeResultApiFuture.get() != null)
                    quantitativeInstrumentDto = modelMapper.map(quantitativeInstrumentForm, AnswerQuantitativeInstrumentDto.class);

            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return quantitativeInstrumentDto;
    }

    @Override
    public List<QuestionDto> findAllQuestion() {
        List<QuestionDto> response = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollectionQuestion().get();

        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                QuestionDto questionDto = new QuestionDto();

                questionDto.setIdQuestion((Long) doc.getData().get(CommonsService.ID_QUESTION));
                questionDto.setDescription(doc.getData().get(CommonsService.DESCRIPTION).toString());
                questionDto.setIdFather((Long) doc.getData().get(CommonsService.ID_FATHER));
                questionDto.setIdQuestionType((Long) doc.getData().get(CommonsService.ID_QUESTION_TYPE));
                questionDto.setOrder((Long) doc.getData().get(CommonsService.ORDER));

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

        docData.put(CommonsService.ID_ANSWER, answer.getIdAnswer());
        docData.put(CommonsService.ID_QUESTION, answer.getIdQuestion());
        docData.put(CommonsService.ID_OPTION_ANSWER, answer.getIdOptionAnswer());
        docData.put(CommonsService.OPEN_ANSWER, answer.getOpenAnswer());
        docData.put(CommonsService.ID_POLL, answer.getIdPoll());

        return docData;
    }
}
