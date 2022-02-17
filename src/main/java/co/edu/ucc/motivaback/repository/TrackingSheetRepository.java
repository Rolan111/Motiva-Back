package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.TrackingSheetEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;

public interface TrackingSheetRepository extends FirestoreReactiveRepository<TrackingSheetEntity> {
    Flux<TrackingSheetEntity> findAll();
}
