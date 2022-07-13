package login20.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import login20.model.LoginUser;

@org.springframework.stereotype.Repository
public interface Repository extends MongoRepository<LoginUser, String> {

}
