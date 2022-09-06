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

        Map<String, Object> docData1 = getDocData(addUser("Chanchi", "Kelly Daniela", "1061801108", "Ck1061801108"));
        Map<String, Object> docData2 = getDocData(addUser("Lucio Esparza", "Joselyne Viviana", "1061820133", "Lj1061820133"));
        Map<String, Object> docData3 = getDocData(addUser("Gómez Jimenez", "Karol Lizeth", "1062754529", "Gk1062754529"));
        Map<String, Object> docData4 = getDocData(addUser("Alomia Bravo", "Andrés Julian", "1061776800", "Aa1061776800"));
        Map<String, Object> docData5 = getDocData(addUser("Camargo Restrepo", "Angie Marcela", "1060877207", "Ca1060877207"));
        Map<String, Object> docData6 = getDocData(addUser("Hurtado Medina", "María Fernanda", "25277453", "Hm25277453"));
        Map<String, Object> docData7 = getDocData(addUser("Giraldo Diaz", "Diana Karina", "31714967", "Gd31714967"));
        Map<String, Object> docData8 = getDocData(addUser("Hoyos Quintero", "Julieth Carolina", "1061800130", "Hj1061800130"));
        Map<String, Object> docData9 = getDocData(addUser("Córdoba Llanten", "Angela Marcela", "1061754099", "Ca1061754099"));
        Map<String, Object> docData10 = getDocData(addUser("Guamanga", "Adriana Lucia", "1059356316", "Ga1059356316"));
        Map<String, Object> docData11 = getDocData(addUser("Ramirez Valencia", "Jhon Jairo", "76327915", "Rj76327915"));
        Map<String, Object> docData12 = getDocData(addUser("Orozco Zuñiga", "Aura Maria", "1061790557", "Oa1061790557"));
        Map<String, Object> docData13 = getDocData(addUser("Cumbalaza Moncada", "Dianne Magyeli", "1061773045", "Md1061773045"));
        Map<String, Object> docData14 = getDocData(addUser("Gomez Flor", "Jennifer Natalia", "1002972339", "Gj1002972339"));
        Map<String, Object> docData15 = getDocData(addUser("Omen Quinayas", "Deiby Socorro", "25485963", "Od25485963"));
        Map<String, Object> docData16 = getDocData(addUser("Muñoz Martinez", "Jessica Fernanda", "1062317907", "Mj1062317907"));
        Map<String, Object> docData17 = getDocData(addUser("Guerrero Valencia", "Karen Jhuliana", "1061600768", "Gk1061600768"));

        ArrayList<Map<String, Object>> listDocData = new ArrayList<>();
        listDocData.add(docData1);
        listDocData.add(docData2);
        listDocData.add(docData3);
        listDocData.add(docData4);
        listDocData.add(docData5);
        listDocData.add(docData6);
        listDocData.add(docData7);
        listDocData.add(docData8);
        listDocData.add(docData9);
        listDocData.add(docData10);
        listDocData.add(docData11);
        listDocData.add(docData12);
        listDocData.add(docData13);
        listDocData.add(docData14);
        listDocData.add(docData15);
        listDocData.add(docData16);
        listDocData.add(docData17);

        for (Map<String, Object> docDataI : listDocData) {
            ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDataI);
            System.out.println(writeResultApiFuture);
        }
    }

    private UserDto addUser(String lastName, String name, String identification, String password) {
        UserDto userDto = new UserDto();
        userDto.setLastName(lastName);
        userDto.setName(name);
        userDto.setIdentification(identification);
        userDto.setJobProfile(UserRolEnum.P_CAMPO);
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