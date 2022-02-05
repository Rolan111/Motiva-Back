package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.PollDto;
import co.edu.ucc.motivaback.service.PollService;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@Service
public class PollServiceImpl implements PollService {
    private final FirebaseInitializer firebase;

    public PollServiceImpl(FirebaseInitializer firebase) {
        this.firebase = firebase;
    }

    @Override
    public Page<PollDto> findAll(Pageable pageable) {
        try {
            List<QueryDocumentSnapshot> documents = getFirebaseCollection(this.firebase, POLL).get().get().getDocuments();

            return gePageFromList(pageable, documents);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public Page<PollDto> findAll(Pageable pageable, Integer idUser) {
        try {
            List<QueryDocumentSnapshot> documents = getFirebaseCollection(this.firebase, POLL).get().get().getDocuments()
                    .stream().filter(doc -> doc.getData().get(ID_USER).equals(idUser)).collect(Collectors.toList());

            return gePageFromList(pageable, documents);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    @Override
    public Page<PollDto> findAll(Pageable pageable, List<Long> idUsers) {
        try {
            List<QueryDocumentSnapshot> documents = new ArrayList<>();
            
            for (Long idUser : idUsers) {
                documents.addAll(getFirebaseCollection(this.firebase, POLL).get().get().getDocuments()
                        .stream().filter(doc -> doc.getData().get(ID_USER).equals(idUser.intValue()))
                        .collect(Collectors.toList()));
            }

            return gePageFromList(pageable, documents);
        } catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private Page<PollDto> gePageFromList(Pageable pageable, List<QueryDocumentSnapshot> documents) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), documents.size());
        List<PollDto> pollDtoList = documents.subList(start, end).stream()
                .map(doc -> getGson().fromJson(getGson().toJson(doc.getData()), PollDto.class))
                .collect(Collectors.toList());

        return new PageImpl<>(pollDtoList, pageable, documents.size());
    }

}
