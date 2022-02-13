package co.edu.ucc.motivaback.repository;


import co.edu.ucc.motivaback.entity.CityEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends FirestoreReactiveRepository<CityEntity> {
}
