package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.UserEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends FirestoreReactiveRepository<UserEntity> {
    Flux<UserEntity> findByUsername(String username);

    Flux<UserEntity> findByIdentification(String identification);

    Flux<UserEntity> findAllByIdSupervisor(Integer idSupervisor);
}
