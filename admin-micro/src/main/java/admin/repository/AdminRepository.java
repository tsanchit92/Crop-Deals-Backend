package admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin.model.AdminModel;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, String> {

}
