package pt.unl.fct.pds.proj1server.repository;

import pt.unl.fct.pds.proj1server.model.MedDataKAnonymous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedDataKAnonymousRepository extends JpaRepository<MedDataKAnonymous, Long> {
    // Custom query methods can be defined here
}
