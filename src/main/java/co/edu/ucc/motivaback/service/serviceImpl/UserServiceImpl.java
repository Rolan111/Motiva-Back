package co.edu.ucc.motivaback.service.serviceImpl;

import co.edu.ucc.motivaback.dto.LoginDto;
import co.edu.ucc.motivaback.payload.LoginForm;
import co.edu.ucc.motivaback.service.FirebaseInitializer;
import co.edu.ucc.motivaback.service.UserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserServiceImpl
 */
@Service
public class UserServiceImpl implements UserService {

    private final FirebaseInitializer firebase;
    private final ModelMapper modelMapper;

    public UserServiceImpl(FirebaseInitializer firebase, ModelMapper modelMapper) {
        this.firebase = firebase;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LoginDto> findAll() {
        List<LoginDto> response = new ArrayList<>();
        LoginDto loginDto;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                loginDto = doc.toObject(LoginDto.class);
                loginDto.setUserId(doc.getId());
                response.add(loginDto);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LoginDto create(LoginForm loginForm) {
        Map<String, Object> docData = getDocData(loginForm);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(loginForm, LoginDto.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LoginDto update(LoginForm loginForm) {
        Map<String, Object> docData = getDocData(loginForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(loginForm.getUserId()).set(docData);
        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(loginForm, LoginDto.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
        try {
            if (writeResultApiFuture.get() != null) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public LoginDto findById(String id) {
        return null;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }

    private Map<String, Object> getDocData(LoginForm loginForm) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("username", loginForm.getUsername());
        docData.put("password", loginForm.getPassword());
        return docData;
    }
}
