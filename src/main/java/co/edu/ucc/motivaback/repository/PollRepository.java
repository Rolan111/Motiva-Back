package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.PollEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends FirestoreReactiveRepository<PollEntity> {
}
