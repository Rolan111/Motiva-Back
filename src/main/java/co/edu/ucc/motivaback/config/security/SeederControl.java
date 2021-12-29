package co.edu.ucc.motivaback.config.security;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.impl.FirebaseInitializer;
import com.google.cloud.firestore.CollectionReference;
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
                new AuthenticatedUser(1L, "a@a.com", ""),
                "key"
        ));
    }

    @EventListener
    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
        this.userSeed();
    }

    protected void userSeed() {
        UserDto userDto = new UserDto();
        userDto.setLast_name("BOLAÑOS");
        userDto.setName("FERNANDO");
        userDto.setIdentification("123456789");
        userDto.setJob_profile(UserRolEnum.PSYCHOLOGIST);
        System.out.println("***************************************************");
        String encode = bCryptPasswordEncoder.encode("motivapw");
        System.out.println(encode);
        userDto.setPassword(encode);
        userDto.setIdentification_type("CC");
        userDto.setCreatedAt(new Date());
        userDto.setCreatedBy(1L);
        userDto.setStatus(RegisterStatusEnum.ACTIVE);

        Map<String, Object> docData = getDocData(userDto);

//        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
//        System.out.println(writeResultApiFuture);
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }

    private Map<String, Object> getDocData(UserDto userDto) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("last_name", userDto.getLast_name());
        docData.put("name", userDto.getName());
        docData.put("identification", userDto.getIdentification());
        docData.put("job_profile", userDto.getJob_profile());
        docData.put("password", userDto.getPassword());
        docData.put("identification_type", userDto.getIdentification_type());
        docData.put("createAt", userDto.getCreatedAt());
        docData.put("createBy", userDto.getCreatedBy());
        docData.put("status", userDto.getStatus());
        return docData;
    }
}