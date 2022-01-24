package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.dto.QuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;
import co.edu.ucc.motivaback.payload.QuantitativeInstrumentForm;
import co.edu.ucc.motivaback.service.QuantitativeInstrumentService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentController
 */
@RestController
@RequestMapping("/api")
public class QuantitativeInstrumentController {
    private final QuantitativeInstrumentService quantitativeInstrumentService;

    public QuantitativeInstrumentController(QuantitativeInstrumentService quantitativeInstrumentService) {
        this.quantitativeInstrumentService = quantitativeInstrumentService;
    }

    @GetMapping(value = "/quantitative-instruments")
    public ResponseEntity<GeneralBodyResponse<List<QuantitativeInstrumentDto>>> getAll() {
        try {
            List<QuantitativeInstrumentDto> postDTOS = this.quantitativeInstrumentService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list answers", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/quantitative-instrument-create")
    public ResponseEntity<GeneralBodyResponse<QuantitativeInstrumentDto>> create(@Valid @RequestBody AnswerQuantitativeInstrumentForm answer) {
        try {
            var quantitativeInstrumentDto = this.quantitativeInstrumentService.create(answer);

            if (quantitativeInstrumentDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(quantitativeInstrumentDto, "created", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "not created", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/quantitative-instrument-update")
    public ResponseEntity<GeneralBodyResponse<QuantitativeInstrumentDto>> update(@Valid @RequestBody QuantitativeInstrumentForm quantitativeInstrumentForm) {
        try {
            var quantitativeInstrumentDto = this.quantitativeInstrumentService.update(quantitativeInstrumentForm);

            return new ResponseEntity<>(new GeneralBodyResponse<>(quantitativeInstrumentDto, "update", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("quantitative-instrument-delete")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@Valid @RequestBody QuantitativeInstrumentForm quantitativeInstrumentForm) {
        try {
            if (this.quantitativeInstrumentService.delete(quantitativeInstrumentForm.getDocumentId()))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, "delete ok", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, "not delete", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("quantitative-instrument/{id}")
    public ResponseEntity<GeneralBodyResponse<QuantitativeInstrumentDto>> findById(@PathVariable String id) {
        try {
            var quantitativeInstrumentDto = this.quantitativeInstrumentService.findById(id);

            return new ResponseEntity<>(new GeneralBodyResponse<>(quantitativeInstrumentDto, "find quantitative-instrument", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantitative-instrument-questions")
    public ResponseEntity<GeneralBodyResponse<List<QuestionDto>>> getAllQuestion() {
        try {
            List<QuestionDto> postDTOS = this.quantitativeInstrumentService.findAllQuestion();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list questions", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}

