package co.edu.ucc.motivaback.service.serviceImpl;

import co.edu.ucc.motivaback.dto.UserDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final FirebaseInitializer firebase;

    public UserDetailsServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDto userDto = new UserDto();

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                if (doc.toObject(UserDto.class).getEmail().equals(email)) {
                    userDto = doc.toObject(UserDto.class);
                    userDto.setId(doc.getId());
                }
            }

            return userDto;

        } catch (Exception e) {
            return null;
        }
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }
}
