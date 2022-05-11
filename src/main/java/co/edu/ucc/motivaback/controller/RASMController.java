package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.RASMEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class RASMController {

    @GetMapping(value = "/rasm")
    public List<RASMEntity> getRASM() throws ExecutionException, InterruptedException {
        List<RASMEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("rasm");
        ApiFuture<QuerySnapshot> future = documentReference.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents) {
            RASMEntity commentsDto = doc.toObject(RASMEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

    @PostMapping(value = "/rasm-create")
    public String saveRASM(@RequestBody RASMEntity rasmEntity) {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("rasm").document().set(rasmEntity);
        return "Hola mundo";
    }
}
