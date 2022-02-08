package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.OptionAnswerDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.dto.QuestionTypeDto;
import co.edu.ucc.motivaback.enums.QuestionTypeEnum;
import co.edu.ucc.motivaback.service.QuestionService;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final FirebaseInitializer firebase;

    public QuestionServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public List<QuestionDto> findAllQuestionary(String type) {
        List<QuestionDto> response = new ArrayList<>();

        try {
            List<QueryDocumentSnapshot> documents = getFirebaseCollection(this.firebase, QUESTION).get().get()
                    .getDocuments().stream()
                    .filter(doc -> doc.getData().get(TYPE).equals(type)).collect(Collectors.toList());

            for (DocumentSnapshot doc : documents) {
                Map<String, Object> data = doc.getData();

                if (data != null) {
                    var questionDto = new QuestionDto();
                    Long idQuestionType = (Long) data.get(ID_QUESTION_TYPE);
                    Long idQuestion = (Long) data.get(ID_QUESTION);

                    questionDto.setIdQuestion(idQuestion);
                    questionDto.setIdQuestionType(idQuestionType);
                    questionDto.setQuestionTypeDto(getQuestionTypeDto(idQuestionType));
                    questionDto.setDescription(data.get(DESCRIPTION).toString());
                    questionDto.setIdFather((Long) data.get(ID_FATHER));
                    questionDto.setOrder((Long) data.get(ORDER));
                    questionDto.setType(data.get(TYPE).toString());

                    if (!questionDto.getQuestionTypeDto().getDescription().equals(QuestionTypeEnum.OPEN.name()))
                        questionDto.setOptionAnswerDtoList(getFirebaseCollection(this.firebase, OPTION_ANSWER).get().get()
                                .getDocuments().stream()
                                .filter(opt -> opt.getData().get(ID_QUESTION).equals(idQuestion))
                                .collect(Collectors.toList()).stream()
                                .map(opt -> getGson().fromJson(getGson().toJson(opt.getData()), OptionAnswerDto.class))
                                .collect(Collectors.toList()));
                    
                    response.add(questionDto);
                }
            }

            return response;
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private QuestionTypeDto getQuestionTypeDto(Long idQuestionType) {
        QuestionTypeDto questionTypeDto = new QuestionTypeDto();

        questionTypeDto.setIdQuestionType(idQuestionType);

        if (idQuestionType == 1) {
            questionTypeDto.setDescription(QuestionTypeEnum.UNIQUE.name());
        } else if (idQuestionType == 2) {
            questionTypeDto.setDescription(QuestionTypeEnum.MULTIPLE.name());
        } else {
            questionTypeDto.setDescription(QuestionTypeEnum.OPEN.name());
        }

        return questionTypeDto;
    }
}
