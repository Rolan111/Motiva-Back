package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.CareSheetDto;
import co.edu.ucc.motivaback.entity.CareSheetAnswerEntity;
import co.edu.ucc.motivaback.entity.CareSheetAnswerPsychosocialEntity;
import co.edu.ucc.motivaback.entity.CareSheetOptionAnwerEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.CareSheetService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")

public class CareSheetController {

    private final CareSheetService careSheetService;

    public CareSheetController(CareSheetService careSheetService) {
        this.careSheetService = careSheetService;
    }

    @GetMapping(value = "/care-sheets")
    public ResponseEntity<GeneralBodyResponse<List<CareSheetDto>>> getAll(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            var careSheetDto = this.careSheetService.getAll();

            if (!careSheetDto.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(careSheetDto, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/care-sheet-create")
    public ResponseEntity<GeneralBodyResponse<CareSheetDto>> save(@Valid @RequestBody CareSheetDto careSheetDto,
                                                                      @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            careSheetDto.setCreatedBy(authenticatedUser.getId().longValue());
            careSheetDto.setIdCareSheet(authenticatedUser.getId());

            var save = this.careSheetService.save(careSheetDto);

            if (save != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(save, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    //ESPACIO DE PRUEBAS

    @GetMapping(value = "/care-sheet-instrument-answers")
    public List<CareSheetAnswerEntity> instrumentAnswers() throws ExecutionException, InterruptedException {
        List<CareSheetAnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            CareSheetAnswerEntity commentsDto = doc.toObject(CareSheetAnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }


    @GetMapping(value = "/care-sheet-options-answers/{id}")
    public List<CareSheetOptionAnwerEntity> opcionesRespuestas(@PathVariable Integer id) throws ExecutionException, InterruptedException {
        List<CareSheetOptionAnwerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("option_answer");
        Query query = documentReference.whereEqualTo("id_option_answer", id);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            CareSheetOptionAnwerEntity commentsDto = doc.toObject(CareSheetOptionAnwerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;

    }

    @GetMapping(value = "/care-sheet-pruebas-consola")
    public void pruebasConsola() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference cities = db.collection("answer");
        Query query = cities.whereEqualTo("id_poll", 29);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            System.out.println(document.getData());
        }

    }

    @GetMapping(value = "/answer-psychosocial-all")
    public List<CareSheetAnswerPsychosocialEntity> answerPsychosocialByIdPoll() throws ExecutionException, InterruptedException {
        List<CareSheetAnswerPsychosocialEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer_psychosocial");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            CareSheetAnswerPsychosocialEntity commentsDto = doc.toObject(CareSheetAnswerPsychosocialEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;

    }

    @GetMapping(value = "/answer-psychosocial-ByIdPoll/{id}")
    public List<CareSheetAnswerPsychosocialEntity> answerPsychosocialByIdPoll(@PathVariable Integer id) throws ExecutionException, InterruptedException {
        List<CareSheetAnswerPsychosocialEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer_psychosocial");
        ApiFuture<QuerySnapshot> future = documentReference.whereEqualTo("id_poll", id).orderBy("id_question").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            CareSheetAnswerPsychosocialEntity commentsDto = doc.toObject(CareSheetAnswerPsychosocialEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;

    }

    @GetMapping(value = "/answer-psychosocial-ByIdPollAndIdQuestion/{idPoll}/{idQuestion}")
    public List<CareSheetAnswerPsychosocialEntity> pollById(@PathVariable Integer idPoll, @PathVariable Integer idQuestion) throws ExecutionException, InterruptedException {
        List<CareSheetAnswerPsychosocialEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer_psychosocial");
        Query query = documentReference.whereEqualTo("id_poll", idPoll).whereEqualTo("id_question", idQuestion);
        ApiFuture<QuerySnapshot> future = query.get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents){
            CareSheetAnswerPsychosocialEntity commentsDto = doc.toObject(CareSheetAnswerPsychosocialEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/care-sheet-answer-psychosocial-create")
    public void saveComment(@RequestBody CareSheetAnswerPsychosocialEntity careSheetAnswerPsychosocialEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("answer_psychosocial").document().set(careSheetAnswerPsychosocialEntity);
    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}
