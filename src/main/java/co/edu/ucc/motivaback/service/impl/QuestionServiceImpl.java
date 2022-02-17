package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.OptionAnswerDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.entity.OptionAnswerEntity;
import co.edu.ucc.motivaback.entity.QuestionEntity;
import co.edu.ucc.motivaback.repository.OptionAnswerRepository;
import co.edu.ucc.motivaback.repository.QuestionRepository;
import co.edu.ucc.motivaback.service.QuestionService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final OptionAnswerRepository optionAnswerRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, OptionAnswerRepository optionAnswerRepository) {
        this.questionRepository = questionRepository;
        this.optionAnswerRepository = optionAnswerRepository;
    }

    @Override
    public List<QuestionDto> findAllQuestionary(String type) {
        List<QuestionEntity> questionEntities = this.questionRepository.findAllByType(type).collectList().block();

        if (questionEntities != null && !questionEntities.isEmpty()) {
            List<QuestionEntity> entityListOrder = questionEntities.stream()
                    .sorted(Comparator.comparingLong(QuestionEntity::getIdQuestion)).collect(Collectors.toList());
            List<QuestionDto> questionDtoList = ObjectMapperUtils.mapAll(entityListOrder, QuestionDto.class);

            questionDtoList.forEach(this::getOptionAnswerEntityList);
            return questionDtoList;
        } else {
            return Collections.emptyList();
        }
    }

    private void getOptionAnswerEntityList(QuestionDto questionDto) {
        List<OptionAnswerEntity> optionAnswerEntityList = this.optionAnswerRepository
                .findAllByIdQuestionAndType(questionDto.getIdQuestion().intValue(), questionDto.getType().toString())
                .collectList().block();

        if (optionAnswerEntityList != null && !optionAnswerEntityList.isEmpty())
            questionDto.setOptionAnswerDtoList(ObjectMapperUtils.mapAll(optionAnswerEntityList, OptionAnswerDto.class));
    }
}
