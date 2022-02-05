package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.AnswerQuantitativeInstrumentDto;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.AnswerQuantitativeInstrumentService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.edu.ucc.motivaback.util.CommonsService.*;

/**
 * @author nagredo
 * @project motiva-back
 * @class QuantitativeInstrumentController
 */
@RestController
@RequestMapping("/api")
public class AnswerQuantitativeInstrumentController {
    private final AnswerQuantitativeInstrumentService answerQuantitativeInstrumentService;

    public AnswerQuantitativeInstrumentController(AnswerQuantitativeInstrumentService answerQuantitativeInstrumentService) {
        this.answerQuantitativeInstrumentService = answerQuantitativeInstrumentService;
    }

    @GetMapping(value = "/quantitative-instruments")
    public ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            List<AnswerQuantitativeInstrumentDto> list = this.answerQuantitativeInstrumentService.findAll();

            if (!list.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/quantitative-instrument-create")
    public ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>> create(
            @Valid @RequestBody List<AnswerQuantitativeInstrumentDto> answerList, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            List<AnswerQuantitativeInstrumentDto> responseList = this.answerQuantitativeInstrumentService.create(answerList);

            if (!responseList.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(responseList, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantitative-instrument-questions")
    public ResponseEntity<GeneralBodyResponse<List<QuestionDto>>> getAllQuestion(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<QuestionDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            List<QuestionDto> allQuestions = this.answerQuantitativeInstrumentService.findAllQuestions();

            if (!allQuestions.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(allQuestions, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantitative-last-sequences")
    public ResponseEntity<GeneralBodyResponse<SequenceDto>> getLastSequence(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<SequenceDto>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            SequenceDto sequences = this.answerQuantitativeInstrumentService.getLastSequences();

            if (sequences != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(sequences, FOUND_OBJECT, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, NOT_FOUND_OBJECT, null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/quantitative-answers-by-poll/{idPoll}")
    public ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>> getAnswersByIdPoll(
            @PathVariable Integer idPoll,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerQuantitativeInstrumentDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            List<AnswerQuantitativeInstrumentDto> list = this.answerQuantitativeInstrumentService.getAnswersByIdPoll(idPoll);

            if (!list.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}

