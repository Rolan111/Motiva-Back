package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.SequenceDto;
import co.edu.ucc.motivaback.entity.AnswerEntity;
import co.edu.ucc.motivaback.entity.CareSheetAnswerEntity;
import co.edu.ucc.motivaback.entity.EmptyPruebaEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.AnswerService;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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


    @GetMapping(value = "/answerByIdPollAndIdQuestion/{idPoll}/{idQuestion}")
    public List<AnswerEntity> answerByIdAndIdQuestion(@PathVariable String idPoll, @PathVariable Integer idQuestion) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", idPoll).whereEqualTo("id_question", idQuestion);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }



    @GetMapping(value = "/answerByIdQuestionAndOpenAnswer/{idQuestion}/{openAnswer}")
    public List<AnswerEntity> answerByIdQuestionAndOpenAnswer(@PathVariable Integer idQuestion, @PathVariable String openAnswer) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_question", idQuestion).whereEqualTo("open_answer", openAnswer);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
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

    //COLECCIÓN DE PRUEBA
    @PostMapping(value = "/answers-pruebas")
    public String saveComment(@RequestBody CareSheetAnswerEntity commentsEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("answer_pruebas").document().set(commentsEntity);
        return "Hola mundo";
    }

    @GetMapping(value = "/answerByIdPoll/{idPoll}")
    public List<AnswerEntity> answerByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/answerByIdQuestion/{idQuestion}")
    public List<AnswerEntity> answerByIdQuestion(@PathVariable Integer idQuestion) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_question", idQuestion);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/answerMultipleByIdPoll/{idPoll}")
    public List<AnswerEntity> answerMultipleConsultas(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        List<AnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", idPoll).whereIn("id_question", Arrays.asList(2,6,1,5));
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AnswerEntity commentsDto = doc.toObject(AnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    //Consulta para los usuarios sin idpoll
    @GetMapping(value = "/answerEmpty/{idQuestion}")
    public List<EmptyPruebaEntity> answerEmpty(@PathVariable Integer idQuestion) throws ExecutionException, InterruptedException {
        List<EmptyPruebaEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_question", idQuestion).whereEqualTo("id_poll", null);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            EmptyPruebaEntity commentsDto = doc.toObject(EmptyPruebaEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @DeleteMapping(value = "/deleteAnswerByIdPoll/{idPoll}")
    public void eliminarAnswerByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        System.out.println("Hemos entrado al proceso de ELIMINADO ANSWER para: " + idPoll);
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            System.out.println("Se va a eliminar el documento: "+doc.getId());
            db.collection("answer").document(doc.getId()).delete();
        }
    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}

