package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmer.model.FarmEmails;

@Repository
public interface FarmEmailRepository  extends JpaRepository<FarmEmails, Integer>{

}
