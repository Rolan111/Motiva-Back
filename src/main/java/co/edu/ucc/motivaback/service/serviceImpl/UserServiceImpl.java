package co.edu.ucc.motivaback.service.serviceImpl;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.payload.UserForm;
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
    public List<UserDto> findAll() {
        List<UserDto> response = new ArrayList<>();
        UserDto userDto;

        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();
        try {
            for (DocumentSnapshot doc : querySnapshotApiFuture.get().getDocuments()) {
                userDto = doc.toObject(UserDto.class);
                userDto.setUserId(doc.getId());
                response.add(userDto);
            }
            return response;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDto create(UserForm userForm) {
        Map<String, Object> docData = getDocData(userForm);

        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docData);

        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(userForm, UserDto.class);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public UserDto update(UserForm userForm) {
        Map<String, Object> docData = getDocData(userForm);
        ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(userForm.getUserId()).set(docData);
        try {
            if (writeResultApiFuture.get() != null) {
                return modelMapper.map(userForm, UserDto.class);
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
    public UserDto findById(String id) {
        return null;
    }

    private CollectionReference getCollection() {
        return firebase.getFirestore().collection("user");
    }

    private Map<String, Object> getDocData(UserForm userForm) {
        Map<String, Object> docData = new HashMap<>();
        docData.put("username", userForm.getUsername());
        docData.put("password", userForm.getPassword());
        return docData;
    }
}
