package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmer.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{

}
