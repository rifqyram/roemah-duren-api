package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, String> {
}
