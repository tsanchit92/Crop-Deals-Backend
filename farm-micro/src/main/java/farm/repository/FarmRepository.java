package farm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import farm.model.FarmModel;

@Repository
public interface FarmRepository extends JpaRepository<FarmModel, String> {

}
