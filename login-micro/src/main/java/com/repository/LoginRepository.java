package com.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.model.UserLogin;

@Repository
public interface LoginRepository extends MongoRepository<UserLogin, String> {
	/* @Query("db.find.inventory{'userName':?0}") */
	Optional<UserLogin> findById(String userName);
	
	
	
	

}
