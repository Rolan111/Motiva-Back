package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.service.AnswerService;
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
public class AnswerServiceImpl implements AnswerService {
    private final FirebaseInitializer firebase;

    public AnswerServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public List<AnswerDto> findAll() {
        List<AnswerDto> response = new ArrayList<>();

        try {
            for (DocumentSnapshot doc : getFirebaseCollection(this.firebase, ANSWER).get().get().getDocuments()) {
                Map<String, Object> map = doc.getData();

                if (map != null) {
                    var answer = new AnswerDto();

                    answer.setIdAnswer((Long) map.get(ID_ANSWER));
                    answer.setIdQuestion((Long) map.get(CommonsService.ID_QUESTION));
                    answer.setOpenAnswer(map.get(CommonsService.OPEN_ANSWER).toString());
                    answer.setIdPoll((Long) map.get(CommonsService.ID_POLL));
                    answer.setIdOptionAnswers((List<Long>) map.get(ID_OPTION_ANSWERS));
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
    public List<AnswerDto> create(List<AnswerDto> quantitativeInstrumentDtos) {
        List<AnswerDto> response = new ArrayList<>();

        for (AnswerDto dto : quantitativeInstrumentDtos) {
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
    public SequenceDto getLastSequences() {
        try {
            int idPoll = getFirebaseCollection(this.firebase, POLL)
                    .get()
                    .get().getDocuments().size();
            int idAnswer = getFirebaseCollection(this.firebase, ANSWER)
                    .get()
                    .get().getDocuments().size();

            return new SequenceDto(idAnswer, idPoll);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public List<AnswerDto> getAnswersByIdPoll(Integer idPoll) {
        try {
            return getFirebaseCollection(this.firebase, ANSWER).get().get().getDocuments()
                    .stream().filter(doc -> doc.getData().get(ID_POLL).equals(idPoll))
                    .map(this::getAnswerQuantitativeInstrumentDto).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private AnswerDto getAnswerQuantitativeInstrumentDto(QueryDocumentSnapshot doc) {
        return getGson().fromJson(getGson().toJson(doc.getData()), AnswerDto.class);
    }

    private Map<String, Object> getDocData(AnswerDto dto) {
        Map<String, Object> docData = new HashMap<>();

        docData.put(ID_ANSWER, dto.getIdAnswer());
        docData.put(ID_QUESTION, dto.getIdQuestion());
        docData.put(OPEN_ANSWER, dto.getOpenAnswer());
        docData.put(ID_POLL, dto.getIdPoll());
        docData.put(ID_OPTION_ANSWERS, dto.getIdOptionAnswers());
        return docData;
    }
}
