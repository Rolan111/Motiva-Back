package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.PollDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.PollService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.edu.ucc.motivaback.util.CommonsService.EMPTY_LIST;
import static co.edu.ucc.motivaback.util.CommonsService.LIST_OK;

/**
 * @author dsolano
 * @project motiva-back
 * @class PollController
 */
@RestController
@RequestMapping("/api")
public class PollController {
    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping(value = "/polls-by-supervisor")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }

        try {
            Page<PollDto> list = this.pollService.findAll(pageable);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/polls-by-pcampo/{idUser}")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAll(@PathVariable Integer idUser,
                                                                     @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }

        try {
            Page<PollDto> list = this.pollService.findAll(pageable, idUser);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
