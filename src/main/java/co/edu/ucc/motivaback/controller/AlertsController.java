package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.AlertEntity;
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

    int tamanioLista;

    @GetMapping(value = "/alerts_size") //Consultamos la cantidad de registros
    public int alertsSize() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert_pruebas");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        System.out.println("El tamanio del array es: "+documents.size());
        tamanioLista = documents.size();
        return tamanioLista;
    }

    @GetMapping(value = "/alert")
    public List<AlertEntity> alerts() throws ExecutionException, InterruptedException {
        List<AlertEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("alert");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            AlertEntity commentsDto = doc.toObject(AlertEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/alert-create")
    public String saveAlerts(@RequestBody AlertEntity alertsEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("alert").document().set(alertsEntity);
        return "Hola mundo";
    }
}
