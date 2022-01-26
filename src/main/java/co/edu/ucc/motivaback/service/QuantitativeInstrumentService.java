package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;
import co.edu.ucc.motivaback.payload.QuantitativeInstrumentForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentService
 */
public interface QuantitativeInstrumentService {
    List<QuantitativeInstrumentDto> findAll();

    AnswerQuantitativeInstrumentDto create(AnswerQuantitativeInstrumentForm answerQuantitativeInstrumentForm);

    QuantitativeInstrumentDto update(QuantitativeInstrumentForm quantitativeInstrumentForm);

    boolean delete(String id);

    QuantitativeInstrumentDto findById(String id);

    List<QuestionDto> findAllQuestion();
}
