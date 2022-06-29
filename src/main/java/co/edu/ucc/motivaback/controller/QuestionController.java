package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.QuestionDto;
import co.edu.ucc.motivaback.entity.QuestionEntityPrueba;
import co.edu.ucc.motivaback.entity.QuestionEntityPrueba;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.QuestionService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static co.edu.ucc.motivaback.util.CommonsService.EMPTY_LIST;
import static co.edu.ucc.motivaback.util.CommonsService.LIST_OK;

/**
 * @author dsolano
 * @project motiva-back
 * @class QuestionController
 */
@RestController
@RequestMapping("/api")
public class QuestionController {
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value = "/questions")
    public ResponseEntity<GeneralBodyResponse<List<QuestionDto>>> getAllQuestion(
            @RequestParam String type,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        var notAccess = (ResponseEntity<GeneralBodyResponse<List<QuestionDto>>>) hasAccess(authenticatedUser);
        if (notAccess != null) return notAccess;

        try {
            List<QuestionDto> allQuestions = this.questionService.findAllQuestionary(type);

            if (!allQuestions.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(allQuestions, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/questionsAdult")
    public List<QuestionEntityPrueba> getAllQuestionsAdult() throws ExecutionException, InterruptedException {
        List<QuestionEntityPrueba> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("question");
        ApiFuture<QuerySnapshot> future = documentReference.whereEqualTo("type","ADULT").orderBy("id_question").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            QuestionEntityPrueba commentsDto = doc.toObject(QuestionEntityPrueba.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/questionByIdQuestion/{id_question}")
    public List<QuestionEntityPrueba> answerPsychosocialByIdPoll(@PathVariable("id_question") Integer idQuestion) throws ExecutionException, InterruptedException {
        List<QuestionEntityPrueba> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("question");
        Query query = documentReference.whereEqualTo("id_question", idQuestion);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            QuestionEntityPrueba commentsDto = doc.toObject(QuestionEntityPrueba.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;

    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

}
