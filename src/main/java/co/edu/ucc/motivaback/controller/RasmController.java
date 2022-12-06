package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.RasmDto;
import co.edu.ucc.motivaback.entity.RASMEntity;
import co.edu.ucc.motivaback.entity.TypeRasmiEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.RASMService;
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

import static co.edu.ucc.motivaback.util.CommonsService.CREATED_FAIL;
import static co.edu.ucc.motivaback.util.CommonsService.CREATED_OK;

@RestController
@RequestMapping("/api")
public class RasmController {

    private final RASMService rasmService;

    public RasmController(RASMService rasmService) {
        this.rasmService = rasmService;
    }


    @GetMapping(value = "/getAllRasm")
    public List<RASMEntity> getRasm() throws ExecutionException, InterruptedException {
        List<RASMEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("rASMEntity");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            RASMEntity commentsDto = doc.toObject(RASMEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @GetMapping(value = "/rasmByIdPoll/{idPoll}")
    public List<RASMEntity> getRASMByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        List<RASMEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("rASMEntity");
        Query query = documentReference.whereEqualTo("id_poll", idPoll);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            RASMEntity commentsDto = doc.toObject(RASMEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    // Para la tabla Type_rasmi
    @GetMapping(value = "/type-rasmi")
    public List<TypeRasmiEntity> getTypeRasmi() throws ExecutionException, InterruptedException {
        List<TypeRasmiEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("type_rasmi");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            TypeRasmiEntity commentsDto = doc.toObject(TypeRasmiEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/rasm-create")
    public ResponseEntity<GeneralBodyResponse<RasmDto>> save(@Valid @RequestBody RasmDto rasmDto,
                                                             @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            rasmDto.setCreatedBy(authenticatedUser.getId().longValue());

            var save = this.rasmService.create(rasmDto);

            if (save != null){
                return new ResponseEntity<>(new GeneralBodyResponse<>(save, CREATED_OK, null), HttpStatus.OK);

                }else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
