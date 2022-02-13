package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.PollEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PollRepository extends FirestoreReactiveRepository<PollEntity> {
    Flux<PollEntity> findAllByOrderByDateDesc();

    Flux<PollEntity> findAllByIdCityOrderByDateDesc(Integer idCity);

    Flux<PollEntity> findAllByIdUserOrderByDateDesc(Integer idUser);

    Flux<PollEntity> findAllByIdUserAndIdCityOrderByDateDesc(Integer idUser, Integer idCity);
}
