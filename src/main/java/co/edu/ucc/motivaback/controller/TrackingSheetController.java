package co.edu.ucc.motivaback.controller;


import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.entity.TrackingSheetEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.repository.TrackingSheetRepository;
import co.edu.ucc.motivaback.service.TrackingSheetService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.spring.data.firestore.FirestoreReactiveOperations;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")

public class TrackingSheetController {

    private final TrackingSheetService trackingSheetService;
    private final TrackingSheetRepository trackingSheetRepository;
    FirestoreReactiveOperations firestoreTemplate2;


    public TrackingSheetController(TrackingSheetService trackingSheetService, TrackingSheetRepository trackingSheetRepository) {
        this.trackingSheetService = trackingSheetService;
        this.trackingSheetRepository = trackingSheetRepository;

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

    @GetMapping(value = "/tracking-sheets2")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> getAll2(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            var trackingSheetDtoList = this.trackingSheetService.getAll2();

                return new ResponseEntity(new GeneralBodyResponse<>(trackingSheetDtoList, LIST_OK, null), HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping(value = "/tracking-sheets3")
    public Flux<TrackingSheetEntity> getAllUsers() {
        return trackingSheetRepository.findAll();
    }

    @GetMapping("/tracking-sheet/{id}")
    public Mono<TrackingSheetEntity> getTrackingById(@PathVariable String id) {
        return trackingSheetRepository.findById(id);
    }

    @GetMapping("/tracking-sheets3/{typeRoute}")
    public Flux<TrackingSheetEntity> getUsersByAge(@PathVariable String typeRoute) {
        return trackingSheetRepository.findByTypeRoute(typeRoute);
    }

    @PostMapping(value = "/tracking-sheet-create")
    public void saveAlerts(@RequestBody TrackingSheetEntity trackingSheetEntity) {
        Firestore db = FirestoreClient.getFirestore();
//        db.collection("tracking_sheet_follow_user").document().set(trackingSheetEntity);
        db.collection("tracking_sheet_follow_user").document().set(trackingSheetEntity);
//        return "Ficha de seguimiento o seguimiento a usuario creada";
    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}
