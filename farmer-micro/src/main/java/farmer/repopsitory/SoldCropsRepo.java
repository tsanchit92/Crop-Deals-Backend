package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farmer.model.SoldCrops;

@Repository
public interface SoldCropsRepo extends JpaRepository<SoldCrops, Integer>{
	

}
