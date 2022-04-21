package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmer.model.BankAccountDeatil;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountDeatil, Integer>{

}
