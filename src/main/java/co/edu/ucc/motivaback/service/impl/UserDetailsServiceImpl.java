package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.util.CommonsService;
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
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            var doc = querySnapshotApiFuture.get().getDocuments()
                    .stream().filter(row -> row.toObject(UserDto.class).getIdentification().equals(identification))
                    .findFirst().orElseThrow(() -> new UsernameNotFoundException(CommonsService.USER_NOT_FOUND));
            var jsonString = CommonsService.getGson().toJson(doc.getData());
            var userDto = CommonsService.getGson().fromJson(jsonString, UserDto.class);

            userDto.setId(doc.getId());
            return userDto;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new UsernameNotFoundException(CommonsService.USER_NOT_FOUND);
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }
}
