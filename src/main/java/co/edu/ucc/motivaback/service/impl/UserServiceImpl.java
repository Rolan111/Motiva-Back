package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.service.UserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

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

    @Override
    public List<UserDto> findAllByIdSupervisor(Integer idSupervisor) {
        try {
            return getFirebaseCollection(this.firebase, USER).get().get().getDocuments()
                    .stream().filter(doc -> doc.getData().get(ID_SUPERVISOR).equals(idSupervisor))
                    .map(this::getUserDto).collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    private UserDto getUserDto(QueryDocumentSnapshot doc) {
        var userDto = getGson().fromJson(getGson().toJson(doc.getData()), UserDto.class);

        userDto.setId(doc.getId());
        return userDto;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection(USER);
    }
}
