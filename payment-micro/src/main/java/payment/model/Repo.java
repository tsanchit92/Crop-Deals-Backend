package payment.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface Repo extends JpaRepository<UserName, Integer> {

}
