package co.edu.ucc.motivaback.controller;


import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.TrackingSheetService;
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
public class TrackingSheetController {
    private final TrackingSheetService trackingSheetService;

    public TrackingSheetController(TrackingSheetService trackingSheetService) {
        this.trackingSheetService = trackingSheetService;
    }

    @GetMapping(value = "/tracking-sheets")
    public ResponseEntity<GeneralBodyResponse<List<TrackingSheetDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            var trackingSheetDtoList = this.trackingSheetService.getAll();

            if (!trackingSheetDtoList.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(trackingSheetDtoList, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/tracking-sheet-create")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> save(@Valid @RequestBody TrackingSheetDto trackingSheetDto,
                                                                      @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            trackingSheetDto.setCreatedBy(authenticatedUser.getId().longValue());
            trackingSheetDto.setIdTrackingSheet(authenticatedUser.getId());

            var save = this.trackingSheetService.save(trackingSheetDto);

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
