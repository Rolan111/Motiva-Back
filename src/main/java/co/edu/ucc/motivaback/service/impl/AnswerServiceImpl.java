package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.OptionAnswerDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.entity.AnswerEntity;
import co.edu.ucc.motivaback.entity.OptionAnswerEntity;
import co.edu.ucc.motivaback.repository.AnswerRepository;
import co.edu.ucc.motivaback.repository.OptionAnswerRepository;
import co.edu.ucc.motivaback.repository.PollRepository;
import co.edu.ucc.motivaback.service.AnswerService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentServiceImpl
 */
@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final PollRepository pollRepository;
    private final OptionAnswerRepository optionAnswerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, PollRepository pollRepository, OptionAnswerRepository optionAnswerRepository) {
        this.answerRepository = answerRepository;
        this.pollRepository = pollRepository;
        this.optionAnswerRepository = optionAnswerRepository;
    }

    @Override
    public List<AnswerDto> findAll() {
        List<AnswerEntity> answerRepositoryAll = this.answerRepository.findAll().collectList().block();

        if (answerRepositoryAll != null && !answerRepositoryAll.isEmpty()) {
            List<AnswerDto> answerDtoList = ObjectMapperUtils.mapAll(answerRepositoryAll, AnswerDto.class);

            answerDtoList.forEach(this::getOptionAnswerEntityList);
            return answerDtoList;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<AnswerDto> create(List<AnswerDto> answerDtoList) {
        List<AnswerEntity> answerEntities = ObjectMapperUtils.mapAll(answerDtoList, AnswerEntity.class);
        List<AnswerEntity> block = this.answerRepository.saveAll(answerEntities).collectList().block();

        if (block != null)
            return ObjectMapperUtils.mapAll(block, AnswerDto.class);
        else
            return Collections.emptyList();
    }

    @Override
    public SequenceDto getLastSequences() {
        var sequenceDto = new SequenceDto();
        Long pollCount = this.pollRepository.count().block();
        Long answerCount = this.answerRepository.count().block();

        sequenceDto.setIdPoll(pollCount != null ? pollCount.intValue() + 1 : -1);
        sequenceDto.setIdAnswer(answerCount != null ? answerCount.intValue() + 1 : -1);
        return sequenceDto;
    }

    @Override
    public List<AnswerDto> getAnswersByIdPoll(Integer idPoll) {
        List<AnswerEntity> answerEntities = this.answerRepository.findAllByIdPoll(idPoll).collectList().block();

        if (answerEntities != null && !answerEntities.isEmpty()) {
            List<AnswerDto> answerDtoList = ObjectMapperUtils.mapAll(answerEntities, AnswerDto.class);

            answerDtoList.forEach(this::getOptionAnswerEntityList);
            return answerDtoList;
        } else {
            return Collections.emptyList();
        }
    }

    private void getOptionAnswerEntityList(AnswerDto answerDto) {
        List<OptionAnswerEntity> optionAnswerEntityList = this.optionAnswerRepository
                .findAllByIdQuestion(answerDto.getIdQuestion().intValue()).collectList().block();

        if (optionAnswerEntityList != null && !optionAnswerEntityList.isEmpty())
            answerDto.setOptionAnswerDtoList(ObjectMapperUtils.mapAll(optionAnswerEntityList, OptionAnswerDto.class));
    }
}
