//package co.edu.ucc.motivaback.config.security;
//
//import co.edu.ucc.motivaback.dto.UserDto;
//import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
//import co.edu.ucc.motivaback.enums.UserRolEnum;
//import co.edu.ucc.motivaback.service.impl.FirebaseInitializer;
//import co.edu.ucc.motivaback.util.CommonsService;
//import com.google.cloud.firestore.CollectionReference;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class SeederControl {
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final FirebaseInitializer firebase;
//
//    public SeederControl(BCryptPasswordEncoder bCryptPasswordEncoder, FirebaseInitializer firebase) {
//        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.firebase = firebase;
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
//                new AuthenticatedUser("123123123", ""),
//                "key"
//        ));
//    }
//
//    @EventListener
//    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
//        this.userSeed();
//    }
//
//    protected void userSeed() {
//        UserDto userDto = new UserDto();
//        userDto.setLastName("BOLAÑOS");
//        userDto.setName("FERNANDO");
//        userDto.setIdentification("123456789");
//        userDto.setJobProfile(UserRolEnum.P_CAMPO);
//        System.out.println("***************************************************");
//        String encode = bCryptPasswordEncoder.encode("motivapw");
//        System.out.println(encode);
//        userDto.setPassword(encode);
//        userDto.setIdentificationType("CC");
//        userDto.setCreatedAt(new Date());
//        userDto.setCreatedBy(1L);
//        userDto.setStatus(RegisterStatusEnum.ACTIVE);
//
//        Map<String, Object> docData = getDocData(userDto);
//
////        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);
////        System.out.println(writeResultApiFuture);
//    }
//
//    private CollectionReference getCollection() {
//        return firebase.getFirestore().collection("user");
//    }
//
//    private Map<String, Object> getDocData(UserDto userDto) {
//        Map<String, Object> docData = new HashMap<>();
//        docData.put(CommonsService.LAST_NAME, userDto.getLastName());
//        docData.put("name", userDto.getName());
//        docData.put("identification", userDto.getIdentification());
//        docData.put("job_profile", userDto.getJobProfile());
//        docData.put("password", userDto.getPassword());
//        docData.put("identification_type", userDto.getIdentificationType());
//        docData.put("createAt", userDto.getCreatedAt());
//        docData.put("createBy", userDto.getCreatedBy());
//        docData.put("status", userDto.getStatus());
//        return docData;
//    }
//}