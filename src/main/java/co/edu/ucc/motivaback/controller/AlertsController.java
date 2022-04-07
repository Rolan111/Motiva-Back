package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.AlertsEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class AlertsController {

    @GetMapping(value = "/alerts")
    public List<AlertsEntity> alerts() throws ExecutionException, InterruptedException {
        List<AlertsEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alerts");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AlertsEntity commentsDto = doc.toObject(AlertsEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/alerts-create")
    public String saveAlerts(@RequestBody AlertsEntity alertsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("alerts").document().set(alertsEntity);
        return "Hola mundo";

    }
}
