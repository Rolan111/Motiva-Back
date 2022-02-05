package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.dto.SequenceDto;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentService
 */
public interface AnswerQuantitativeInstrumentService {
    List<AnswerQuantitativeInstrumentDto> findAll();

    List<AnswerQuantitativeInstrumentDto> create(List<AnswerQuantitativeInstrumentDto> answerQuantitativeInstrumentDto);

    List<QuestionDto> findAllQuestions();

    SequenceDto getLastSequences();
}
