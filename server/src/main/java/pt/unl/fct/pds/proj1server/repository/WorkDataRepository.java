package pt.unl.fct.pds.proj1server.repository;

import pt.unl.fct.pds.proj1server.model.WorkData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDataRepository extends JpaRepository<WorkData, Long> {

    long countByPostalCode(String postalCode);

    long countByGender(String gender);

    long countByEducation(String education);

    long countByWorkplace(String workplace);

    long countByDepartment(String department);
}
