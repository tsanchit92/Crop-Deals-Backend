package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.repository.LoginRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = LoginRepository.class)
public class LoginMicroApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginMicroApplication.class, args);
	}

}
