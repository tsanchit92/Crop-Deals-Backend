package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmer.model.FarmerModel;

@Repository
public interface FarmerRepository extends JpaRepository<FarmerModel, String> {

}
