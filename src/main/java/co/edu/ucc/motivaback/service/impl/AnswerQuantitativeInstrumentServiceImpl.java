package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.service.AnswerQuantitativeInstrumentService;
import co.edu.ucc.motivaback.util.CommonsService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentServiceImpl
 */
@Service
public class AnswerQuantitativeInstrumentServiceImpl implements AnswerQuantitativeInstrumentService {
    private final FirebaseInitializer firebase;

    public AnswerQuantitativeInstrumentServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public List<AnswerQuantitativeInstrumentDto> findAll() {
        List<AnswerQuantitativeInstrumentDto> response = new ArrayList<>();

        try {
            for (DocumentSnapshot doc : getFirebaseCollection(this.firebase, ANSWER).get().get().getDocuments()) {
                Map<String, Object> map = doc.getData();

                if (map != null) {
                    var answer = new AnswerQuantitativeInstrumentDto();

                    answer.setIdAnswer((Integer) map.get(CommonsService.ID_ANSWER));
                    answer.setIdQuestion((Integer) map.get(CommonsService.ID_QUESTION));
                    answer.setIdOptionAnswer((Integer) map.get(CommonsService.ID_OPTION_ANSWER));
                    answer.setOpenAnswer(map.get(CommonsService.OPEN_ANSWER).toString());
                    answer.setIdPoll((Integer) map.get(CommonsService.ID_POLL));
                    response.add(answer);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }

        return response;
    }

    @Override
    public List<AnswerQuantitativeInstrumentDto> create(List<AnswerQuantitativeInstrumentDto> quantitativeInstrumentDtos) {
        List<AnswerQuantitativeInstrumentDto> response = new ArrayList<>();

        for (AnswerQuantitativeInstrumentDto dto : quantitativeInstrumentDtos) {
            ApiFuture<WriteResult> writeResultApiFuture = getFirebaseCollection(this.firebase, ANSWER).document().
                    create(getDocData(dto));

            try {
                if (writeResultApiFuture.get() != null)
                    response.add(dto);
            } catch (ExecutionException | InterruptedException e) {
                Thread.currentThread().interrupt();
                return Collections.emptyList();
            }
        }

        return response;
    }

    @Override
    public List<QuestionDto> findAllQuestions() {
        List<QuestionDto> response = new ArrayList<>();

        try {
            for (DocumentSnapshot doc : getFirebaseCollection(this.firebase, QUESTION).get().get().getDocuments()) {
                Map<String, Object> data = doc.getData();

                if (data != null) {
                    var questionDto = new QuestionDto();

                    questionDto.setIdQuestion((Long) data.get(ID_QUESTION));
                    questionDto.setDescription(data.get(DESCRIPTION).toString());
                    questionDto.setIdFather((Long) data.get(ID_FATHER));
                    questionDto.setIdQuestionType((Long) data.get(ID_QUESTION_TYPE));
                    questionDto.setOrder((Long) data.get(ORDER));
                    response.add(questionDto);
                }
            }
            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public SequenceDto getLastSequences() {
        try {
            int idPoll = getFirebaseCollection(this.firebase, POLL)
                    .get()
                    .get().getDocuments().size() + 1;
            int idAnswer = getFirebaseCollection(this.firebase, ANSWER)
                    .get()
                    .get().getDocuments().size() + 1;

            return new SequenceDto(idAnswer, idPoll);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public List<AnswerQuantitativeInstrumentDto> getAnswersByIdPoll(Integer idPoll) {
        try {
            return getFirebaseCollection(this.firebase, ANSWER).get().get().getDocuments()
                    .stream().filter(doc -> doc.getData().get(ID_POLL).equals(idPoll))
                    .map(this::getAnswerQuantitativeInstrumentDto).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private AnswerQuantitativeInstrumentDto getAnswerQuantitativeInstrumentDto(QueryDocumentSnapshot doc) {
        return getGson().fromJson(getGson().toJson(doc.getData()), AnswerQuantitativeInstrumentDto.class);
    }

    private Map<String, Object> getDocData(AnswerQuantitativeInstrumentDto dto) {
        Map<String, Object> docData = new HashMap<>();

        docData.put(ID_ANSWER, dto.getIdAnswer());
        docData.put(ID_QUESTION, dto.getIdQuestion());
        docData.put(ID_OPTION_ANSWER, dto.getIdOptionAnswer());
        docData.put(OPEN_ANSWER, dto.getOpenAnswer());
        docData.put(ID_POLL, dto.getIdPoll());
        return docData;
    }
}
