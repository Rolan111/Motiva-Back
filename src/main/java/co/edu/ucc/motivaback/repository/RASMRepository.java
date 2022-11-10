package co.edu.ucc.motivaback.repository;

import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.entity.RASMEntity;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertRepository
 */
@Repository
public interface RASMRepository extends FirestoreReactiveRepository<RASMEntity> {
}
