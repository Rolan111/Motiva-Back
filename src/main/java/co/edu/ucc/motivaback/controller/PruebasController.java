package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.entity.PruebasEntity;
import co.edu.ucc.motivaback.entity.PruebasEntity2;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")

public class PruebasController {

    @GetMapping(value = "/pruebasByTime/{idPoll}")
    public List<PruebasEntity2> alertByIdPoll(@PathVariable String idPoll) throws ExecutionException, InterruptedException {
        String capturandoFechas = "comienzo";
        List<PruebasEntity2> pruebasEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("answer");
        Query query = documentReference.whereEqualTo("id_poll", null);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
//            doc.toObject();
            PruebasEntity2 pruebasEntity = doc.toObject(PruebasEntity2.class);
//            prueba2.setDatosPantalla("Rooolan");
            pruebasEntities.add(pruebasEntity);
            pruebasEntity.setCreatedAt(doc.getCreateTime().toString());
        }
        pruebasEntities.sort(Comparator.comparing(PruebasEntity2::getCreatedAt));
        return pruebasEntities;
    }

}
