package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.TrackingSheetEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TrackingSheetRepository extends FirestoreReactiveRepository<TrackingSheetEntity> {
    Flux<TrackingSheetEntity> findAll();
    Mono<TrackingSheetEntity> findById(String idDocuementTrackingSheet);
    Flux<TrackingSheetEntity> findByTypeRoute(String typeRoute);

}
