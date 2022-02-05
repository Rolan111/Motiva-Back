package co.edu.ucc.motivaback.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author nagredo
 * @project motiva-back
 * @class FirebaseInitializer
 */
@Service
public class FirebaseInitializer {

    @Value("${server.firebase.url}")
    private String databaseUrl;
    @Value("${server.firebase.key}")
    private String firebaseKey;

    @PostConstruct
    private void iniFirestore() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream(firebaseKey);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(databaseUrl)
                .build();

        if (FirebaseApp.getApps().isEmpty())
            FirebaseApp.initializeApp(options);
    }

    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
