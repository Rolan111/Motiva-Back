package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.UserDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final FirebaseInitializer firebase;

    public UserDetailsServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            var doc = querySnapshotApiFuture.get().getDocuments()
                    .stream().filter(row -> row.toObject(UserDto.class).getEmail().equals(email))
                    .findFirst().orElseThrow(() -> new UsernameNotFoundException("El usuario no existe"));
            var userDto = doc.toObject(UserDto.class);

            userDto.setId(doc.getId());
            return userDto;

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new UsernameNotFoundException("El usuario no existe");
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }
}
