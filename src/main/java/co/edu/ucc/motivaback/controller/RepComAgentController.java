package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.RepComAgentService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")
public class RepComAgentController {

    private final RepComAgentService repComAgentService;

    public RepComAgentController(RepComAgentService repComAgentService) {
        this.repComAgentService = repComAgentService;
    }

    @GetMapping(value = "/rep-com-agents")
    public ResponseEntity<GeneralBodyResponse<List<RepComAgentDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            var repComAgentDtoList = this.repComAgentService.getAll();

            if (!repComAgentDtoList.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(repComAgentDtoList, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/rep-com-agent-create")
    public ResponseEntity<GeneralBodyResponse<RepComAgentDto>> save(@Valid @RequestBody RepComAgentDto repComAgentDto,
                                                                    @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            repComAgentDto.setCreatedBy(authenticatedUser.getId().longValue());
            //repComAgentDto.setIdRepComAgent(authenticatedUser.getId());

            var save = this.repComAgentService.save(repComAgentDto);

            if (save != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(save, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
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
