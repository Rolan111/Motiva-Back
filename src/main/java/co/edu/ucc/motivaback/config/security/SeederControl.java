package co.edu.ucc.motivaback.config.security;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class SeederControl {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FirebaseInitializer firebase;

    public SeederControl(BCryptPasswordEncoder bCryptPasswordEncoder, FirebaseInitializer firebase) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.firebase = firebase;
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(1L, "a@a.com"),
                "key"
        ));
    }

    @EventListener
    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
        this.userSeed();
    }

    protected void userSeed() {
        UserDto userDto = new UserDto();
        userDto.setEmail("a@b.com");
        userDto.setFullName("Test test");
        userDto.setIdentification("5");
        userDto.setUserRolEnum(UserRolEnum.ADMIN);
        System.out.println("***************************************************");
        String encode = bCryptPasswordEncoder.encode("pruebas");
        System.out.println(encode);
        userDto.setPassword(encode);
        userDto.setPhone("456456");
        userDto.setCreatedAt(new Date());
        userDto.setCreatedBy(1L);
        userDto.setStatus(RegisterStatusEnum.ACTIVE);

        Map<String, Object> docData = getDocData(userDto);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
        System.out.println(writeResultApiFuture);
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }

    private Map<String, Object> getDocData(UserDto userDto) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("email", userDto.getEmail());
        docData.put("fullname", userDto.getFullName());
        docData.put("identification", userDto.getIdentification());
        docData.put("userRol", userDto.getUserRolEnum());
        docData.put("password", userDto.getPassword());
        docData.put("phone", userDto.getPhone());
        docData.put("createAt", userDto.getCreatedAt());
        docData.put("createBy", userDto.getCreatedBy());
        docData.put("status", userDto.getStatus());
        return docData;
    }
}
