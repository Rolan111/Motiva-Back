package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentService
 */
public interface QuantitativeInstrumentService {
    List<AnswerQuantitativeInstrumentDto> findAll();

    AnswerQuantitativeInstrumentDto create(List<AnswerQuantitativeInstrumentForm> answerQuantitativeInstrumentForm);

    List<QuestionDto> findAllQuestion();
}
