package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.AnswerEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AnswerRepository extends FirestoreReactiveRepository<AnswerEntity> {
    Flux<AnswerEntity> findAllByIdPoll(Integer idPoll);
}
