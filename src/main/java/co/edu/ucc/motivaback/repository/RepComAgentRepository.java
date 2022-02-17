package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.RepComAgentEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface RepComAgentRepository extends FirestoreReactiveRepository<RepComAgentEntity> {
    Flux<RepComAgentEntity> findAll();
}
