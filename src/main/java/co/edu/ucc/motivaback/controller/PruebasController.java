package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.entity.PruebasEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")

public class PruebasController {

    @GetMapping(value = "/pruebasByTime/{idPoll}")
    public List<PruebasEntity> alertByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        List<PruebasEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("poll");
        Query query = documentReference.whereEqualTo("id_poll", null).orderBy("created_by");
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            PruebasEntity commentsDto = doc.toObject(PruebasEntity.class);
            commentsEntities.add(commentsDto);
            System.out.println(doc.getCreateTime());
        }
        return commentsEntities;
    }

}
