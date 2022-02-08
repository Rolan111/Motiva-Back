package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.SequenceDto;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentService
 */
public interface AnswerService {
    List<AnswerDto> findAll();

    List<AnswerDto> create(List<AnswerDto> answerDto);

    SequenceDto getLastSequences();

    List<AnswerDto> getAnswersByIdPoll(Integer idPoll);
}
