package farmer.repopsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import farmer.model.Crop;

@Repository
public interface CropRespository extends JpaRepository<Crop, Integer>{
	
	

}
