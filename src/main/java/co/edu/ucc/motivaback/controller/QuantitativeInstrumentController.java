package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.payload.AnswerQuantitativeInstrumentForm;
import co.edu.ucc.motivaback.service.QuantitativeInstrumentService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>> getAll() {
        try {
            List<AnswerQuantitativeInstrumentDto> postDTOS = this.quantitativeInstrumentService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list answers", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/quantitative-instrument-create")
    public ResponseEntity<GeneralBodyResponse<AnswerQuantitativeInstrumentDto>> create(@Valid @RequestBody List<AnswerQuantitativeInstrumentForm> answer) {
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

    @GetMapping(value = "/quantitative-instrument-questions")
    public ResponseEntity<GeneralBodyResponse<List<QuestionDto>>> getAllQuestion(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {

        if (authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) || authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            try {
                List<QuestionDto> postDTOS = this.quantitativeInstrumentService.findAllQuestion();

                if (!postDTOS.isEmpty())
                    return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list questions", null), HttpStatus.OK);
                else
                    return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

            } catch (Exception ex) {
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }

    }
}

