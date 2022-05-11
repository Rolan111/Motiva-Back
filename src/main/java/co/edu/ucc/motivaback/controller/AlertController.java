package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.config.security.AuthenticatedUser;
import co.edu.ucc.motivaback.dto.AlertDto;
import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.entity.InactiveAlertEntity;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.AlertService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
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
public class AlertController {

    private final AlertService alertEntity;

    public AlertController(AlertService alertEntity) {
        this.alertEntity = alertEntity;
    }

    int tamanioLista;

    @GetMapping(value = "/alerts-size") //Consultamos la cantidad de registros
    public int alertsSize() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert_pruebas");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        tamanioLista = documents.size();
        return tamanioLista;
    }

    @GetMapping(value = "/alerts")
    public List<AlertEntity> getAlerts() throws ExecutionException, InterruptedException {
        List<AlertEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            AlertEntity commentsDto = doc.toObject(AlertEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    //GET ALERTS de PRUEBA
    @GetMapping(value = "/alerts-pruebas")
    public List<AlertEntity> getAlertsPrueba() throws ExecutionException, InterruptedException {
        List<AlertEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert_pruebas");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            AlertEntity commentsDto = doc.toObject(AlertEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/alert-create")
    public ResponseEntity<GeneralBodyResponse<AlertDto>> save(@Valid @RequestBody AlertDto alertDto,
                                                              @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        if (!authenticatedUser.getRol().equals(UserRolEnum.SUPERVISOR.name()) && !authenticatedUser.getRol().equals(UserRolEnum.P_CAMPO.name()))
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, CommonsService.NOT_ACCESS, null), HttpStatus.UNAUTHORIZED);

        try {
            alertDto.setCreatedBy(authenticatedUser.getId().longValue());

            var save = this.alertEntity.create(alertDto);

            if (save != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(save, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/alert-create-pruebas")
    public String saveAlerts(@RequestBody AlertEntity alertsEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("alert_pruebas").document().set(alertsEntity);
        return "Hola mundo";
    }

    @DeleteMapping(value = "/alert-delete/{idPoll}")
    public List<AlertEntity> deleteAlerts(@PathVariable Integer idPoll) throws ExecutionException, InterruptedException {
        List<AlertEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert_pruebas");
        ApiFuture<QuerySnapshot> future = documentReference.whereEqualTo("id_poll",idPoll).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            AlertEntity commentsDto = doc.toObject(AlertEntity.class);
            commentsEntities.add(commentsDto);
            doc.getReference().delete();
        }
        return commentsEntities;
    }
    //********************

    //INACTIVE ALERTS
    @GetMapping(value = "/inactive-alert")
    public List<InactiveAlertEntity> getInactiveAlerts() throws ExecutionException, InterruptedException {
        List<InactiveAlertEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("inactive_alert");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            InactiveAlertEntity commentsDto = doc.toObject(InactiveAlertEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/inactive-alert-create")
    public String saveComment(@RequestBody InactiveAlertEntity inactiveAlertEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("inactive_alert").document().set(inactiveAlertEntity);
        return "Hola mundo";
    }

}
