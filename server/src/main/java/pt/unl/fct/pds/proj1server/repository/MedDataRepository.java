package pt.unl.fct.pds.proj1server.repository;

import pt.unl.fct.pds.proj1server.model.MedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedDataRepository extends JpaRepository<MedData, Long> {
    long countByName(String name);

    long countByAge(int age);

    long countByAddress(String address);

    long countByEmail(String email);

    long countByGender(String gender);

    long countByPostalCode(String postalCode);

    long countByDiagnosis(String diagnosis);
}
