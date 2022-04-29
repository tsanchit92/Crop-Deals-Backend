package admin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import admin.model.AdminModel;

@Repository
public interface AdminRepository extends MongoRepository<AdminModel, String> {

}
