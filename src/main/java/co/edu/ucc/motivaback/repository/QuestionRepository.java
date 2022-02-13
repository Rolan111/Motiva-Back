package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.QuestionEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface QuestionRepository extends FirestoreReactiveRepository<QuestionEntity> {
    Flux<QuestionEntity> findAllByType(String type);
}
