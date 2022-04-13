package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.entity.InactiveAlertEntity;
import co.edu.ucc.motivaback.entity.RASMEntity;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class RASMController {

    @PostMapping(value = "/rasm-create")
    public String saveComment(@RequestBody RASMEntity rasmEntity) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        db.collection("rasm").document().set(rasmEntity);
        return "Hola mundo";
    }
}
