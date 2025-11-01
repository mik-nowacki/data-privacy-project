package pt.unl.fct.pds.proj1server.repository;

import pt.unl.fct.pds.proj1server.model.MedDataDeidentified;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedDataDeidentifiedRepository extends JpaRepository<MedDataDeidentified, Long> {
    // Custom query methods can be defined here
}
