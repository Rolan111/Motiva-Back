package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.AnswerService;
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
 * @class AnswerController
 */
@RestController
@RequestMapping("/api")
public class AnswerController {
    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @GetMapping(value = "/answers")
    public ResponseEntity<GeneralBodyResponse<List<AnswerDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            var list = this.answerService.findAll();

            if (!list.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/answer")
    public ResponseEntity<GeneralBodyResponse<List<AnswerDto>>> create(
            @Valid @RequestBody List<AnswerDto> answerList, @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            var responseList = this.answerService.create(answerList);

            if (!responseList.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(responseList, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/last-sequences")
    public ResponseEntity<GeneralBodyResponse<SequenceDto>> getLastSequence(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<SequenceDto>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            var sequences = this.answerService.getLastSequences();

            if (sequences != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(sequences, FOUND_OBJECT, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, NOT_FOUND_OBJECT, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/answers-by-poll/{idPoll}")
    public ResponseEntity<GeneralBodyResponse<List<AnswerDto>>> getAnswersByIdPoll(
            @PathVariable Integer idPoll,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<AnswerDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            var list = this.answerService.getAnswersByIdPoll(idPoll);

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

