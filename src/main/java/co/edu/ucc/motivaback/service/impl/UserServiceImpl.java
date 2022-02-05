package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.service.UserService;
import co.edu.ucc.motivaback.util.CommonsService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static co.edu.ucc.motivaback.util.CommonsService.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {
    private final FirebaseInitializer firebase;

    public UserServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public UserDto findByUsername(String username) throws UsernameNotFoundException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        try {
            var doc = querySnapshotApiFuture.get().getDocuments()
                    .stream().filter(row -> row.toObject(UserDto.class).getUsername().equals(username))
                    .findFirst().orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

            return getUserDto(doc);

        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new UsernameNotFoundException(USER_NOT_FOUND);
        }
    }

    private UserDto getUserDto(QueryDocumentSnapshot doc) {
        var jsonString = CommonsService.getGson().toJson(doc.getData());
        UserDto userDto = CommonsService.getGson().fromJson(jsonString, UserDto.class);

        userDto.setId(doc.getId());
        return userDto;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }
}
