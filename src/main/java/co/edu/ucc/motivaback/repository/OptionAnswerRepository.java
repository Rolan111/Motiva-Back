package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.OptionAnswerEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OptionAnswerRepository extends FirestoreReactiveRepository<OptionAnswerEntity> {
    Flux<OptionAnswerEntity> findAllByIdQuestionAndType(Integer idQuestion, String type);
}
