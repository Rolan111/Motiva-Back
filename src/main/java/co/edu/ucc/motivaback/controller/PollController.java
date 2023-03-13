package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.PollDto;
import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.entity.PollEntity;
import co.edu.ucc.motivaback.entity.PruebasEntityAnswerPsychosocial;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.PollService;
import co.edu.ucc.motivaback.service.UserService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

/**
 * @author dsolano
 * @project motiva-back
 * @class PollController
 */
@RestController
@RequestMapping("/api")
public class PollController {
    private final PollService pollService;
    private final UserService userService;

    public PollController(PollService pollService, UserService userService) {
        this.pollService = pollService;
        this.userService = userService;
    }

    @GetMapping(value = "/polls")
    public List<PollEntity> polls() throws ExecutionException, InterruptedException {
        List<PollEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        ApiFuture<QuerySnapshot> future = documentReference.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents){
            PollEntity commentsDto = doc.toObject(PollEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/polls-by-supervisor")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            var idUsers = this.userService.findAllByIdSupervisor(authenticatedUser.getId())
                    .stream().map(UserDto::getIdUser).collect(Collectors.toList());
            Page<PollDto> list = this.pollService.findAllByIdUsers(pageable, idUsers);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/polls-by-pcampo/{idUser}")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAllByIdUser(@PathVariable Integer idUser,
                                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            Page<PollDto> list = this.pollService.findAllByIdUser(pageable, idUser);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/pollByIdPoll/{idPoll}")
    public List<PollEntity> pollById(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        List<PollEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents){
            PollEntity commentsDto = doc.toObject(PollEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/polls-by-city/{idCity}")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAllByIdCity(@PathVariable Integer idCity,
                                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            Page<PollDto> list = this.pollService.findAllByIdCity(pageable, idCity);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/polls-by-user/{idUser}/city/{idCity}")
    public ResponseEntity<GeneralBodyResponse<Page<PollDto>>> getAllByIdUserAndIdCity(@PathVariable Integer idUser, @PathVariable Integer idCity,
                                                                                      @AuthenticationPrincipal AuthenticatedUser authenticatedUser, Pageable pageable) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            Page<PollDto> list = this.pollService.findAllByIdCityAndIdUser(pageable, idCity, idUser);

            if (!list.getContent().isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(list, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "poll")
    public ResponseEntity<GeneralBodyResponse<PollDto>> save(@Valid @RequestBody PollDto pollDto,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            pollDto.setCreatedBy(authenticatedUser.getId().longValue());
            pollDto.setIdUser(authenticatedUser.getId());

            var save = this.pollService.save(pollDto);

            if (save != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(save, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    //Listas de datos POLLs
    @PostMapping(value = "/polls-create")
    public void saveComment(@RequestBody List<PollEntity> pollEntity) {
        Firestore db = FirestoreClient.getFirestore();

        for (PollEntity doc: pollEntity){
            db.collection("poll").document().set(doc);
        }
    }

    @DeleteMapping(value = "/deletePollByIdPoll/{idPoll}")
    public void eliminarPollByIdPoll(String idPoll) throws ExecutionException, InterruptedException {
        System.out.println("Hemos entrado al proceso de ELIMINADO POLL para: "+idPoll);
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            System.out.println("Se va a eliminar el documento: "+doc.getId());
            db.collection("poll").document(doc.getId()).delete();
        }
    }

}
