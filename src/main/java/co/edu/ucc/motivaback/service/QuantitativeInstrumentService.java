package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.QuantitativeInstrumentDto;
import co.edu.ucc.motivaback.payload.QuantitativeInstrumentForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentService
 */
public interface QuantitativeInstrumentService {
    List<QuantitativeInstrumentDto> findAll();

    QuantitativeInstrumentDto create(QuantitativeInstrumentForm quantitativeInstrumentForm);

    QuantitativeInstrumentDto update(QuantitativeInstrumentForm quantitativeInstrumentForm);

    boolean delete(String id);

    QuantitativeInstrumentDto findById(String id);
}
