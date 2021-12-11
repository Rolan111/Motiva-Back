package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.UserDto;
import com.google.cloud.firestore.DocumentReference;
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
        DocumentReference user = this.firebase.getFirestore().collection("user").document(email);

        return new UserDto();
    }
}
