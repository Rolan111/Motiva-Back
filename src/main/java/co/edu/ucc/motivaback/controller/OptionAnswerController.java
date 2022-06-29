package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.OptionAnswerEntity;
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
public class OptionAnswerController {

    @GetMapping(value = "/optionAnswerByIdOptionAnswer/{idOptionAnswer}")
    public List<OptionAnswerEntity> optionAnswerByIdOptionAnswer(@PathVariable Integer idOptionAnswer) throws ExecutionException, InterruptedException {
        List<OptionAnswerEntity> commentsEntities = new ArrayList<>();

        Firestore db = FirestoreClient.getFirestore();
        CollectionReference documentReference = db.collection("option_answer");
        Query query = documentReference.whereEqualTo("id_option_answer", idOptionAnswer);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        for (QueryDocumentSnapshot doc : documents){
            OptionAnswerEntity commentsDto = doc.toObject(OptionAnswerEntity.class);
            commentsEntities.add(commentsDto);
        }
        return commentsEntities;
    }

}
