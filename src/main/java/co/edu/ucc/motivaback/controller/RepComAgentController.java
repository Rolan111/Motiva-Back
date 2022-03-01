package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.CommentsDto;
import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.entity.CommentsEntity;
import co.edu.ucc.motivaback.entity.RepComAgentEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.repository.RepComAgentRepository;
import co.edu.ucc.motivaback.service.RepComAgentService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")
public class RepComAgentController {

    private final RepComAgentService repComAgentService;
    private final RepComAgentRepository repComAgentRepository;

    public RepComAgentController(RepComAgentService repComAgentService, RepComAgentRepository repComAgentRepository) {
        this.repComAgentService = repComAgentService;
        this.repComAgentRepository = repComAgentRepository;
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

    @GetMapping("/rep-com-agent/{id}")
    private Mono<RepComAgentEntity> getReportById(@PathVariable String id) {
        return repComAgentRepository.findById(id);
    }


    @GetMapping(value = "/rep-com-agent-forum-comments/{idReport}")

    private List<CommentsEntity> getSubcollection(@PathVariable String idReport) throws ExecutionException, InterruptedException {
        List<CommentsEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("rep_com_agent").document(idReport)
                .collection("comments");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            CommentsEntity commentsDto = doc.toObject(CommentsEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;

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

    @PostMapping(value = "/rep-com-agent-comments-create/{id}")
    public String saveComment(@PathVariable String id, @RequestBody CommentsEntity commentsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        //ApiFuture<WriteResult> collectionApiFuture = db.collection("rep_com_agent").document(id).collection("comments").document().set(commentsDto);
        db.collection("rep_com_agent").document(id).collection("comments").document().set(commentsEntity);
        return "Hola mundo";
        //return collectionApiFuture.get().getUpdateTime().toString();
    }

    private ResponseEntity<?> hasAccess(AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()) && !authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name())) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);
        }
        return null;
    }


}
