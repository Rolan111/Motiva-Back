/*
package co.edu.ucc.motivaback.config.security;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import co.edu.ucc.motivaback.service.impl.FirebaseInitializer;
import co.edu.ucc.motivaback.util.CommonsService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static co.edu.ucc.motivaback.enums.UserRolEnum.P_CAMPO;
import static co.edu.ucc.motivaback.enums.UserRolEnum.SUPERVISOR;

@Component
public class SeederControl {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FirebaseInitializer firebase;



    public SeederControl(BCryptPasswordEncoder bCryptPasswordEncoder, FirebaseInitializer firebase) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.firebase = firebase;
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser("123123123", "", 1),
                "key"
        ));
    }

    @EventListener
    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
        this.userSeed();
    }

    protected void userSeed() {

        Map<String, Object> docData1 = getDocData(addUserPCampo(1L,"Chanchi", "Kelly Daniela", "1061801108", "Ck1061801108", P_CAMPO));
        Map<String, Object> docData2 = getDocData(addUserPCampo(2L,"Campo", "Daniel", "1020604689", "Cd1020604689", SUPERVISOR));


        ArrayList<Map<String, Object>> listDocData = new ArrayList<>();
        listDocData.add(docData1);
        listDocData.add(docData2);


        for (Map<String, Object> docDataI : listDocData) {
            ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDataI);
            System.out.println(writeResultApiFuture);
        }
    }

    private UserDto addUserPCampo(Long idUser,String lastName, String name, String identification, String password, UserRolEnum jobProfile) {
        UserDto userDto = new UserDto();
        userDto.setIdUser(idUser);
        userDto.setLastName(lastName);
        userDto.setName(name);
        userDto.setIdentification(identification);
        userDto.setJobProfile(jobProfile);
        System.out.println("*****************");
        String encode = bCryptPasswordEncoder.encode(password);
        System.out.println(encode);
        userDto.setPassword(encode);
        userDto.setIdentificationType("CC");
        userDto.setCreatedAt(new Date());
        userDto.setCreatedBy(1L);
        userDto.setStatus(RegisterStatusEnum.ACTIVE);
        return userDto;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }

    private Map<String, Object> getDocData(UserDto userDto) {
        Map<String, Object> docData = new HashMap<>();
        docData.put(CommonsService.LAST_NAME, userDto.getLastName());
        docData.put("id_user", userDto.getIdUser());
        docData.put("name", userDto.getName());
        docData.put("identification", userDto.getIdentification());
        docData.put("job_profile", userDto.getJobProfile());
        docData.put("password", userDto.getPassword());
        docData.put("identification_type", userDto.getIdentificationType());
        docData.put("createAt", userDto.getCreatedAt());
        docData.put("createBy", userDto.getCreatedBy());
        docData.put("status", userDto.getStatus());
        return docData;
    }
}
*/
