package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.CareSheetEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface CareSheetRepository extends FirestoreReactiveRepository<CareSheetEntity> {
    Flux<CareSheetEntity> findAll();
}
