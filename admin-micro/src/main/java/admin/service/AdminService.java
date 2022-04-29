package admin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AdminService {
	
	WebClient webClient =WebClient.create("http://localhost:11000");
	WebClient webClient2 =WebClient.create("http://localhost:8000");

	public Boolean deleteFarm(int farmId) {
		
		return webClient.delete()
				.uri("/farm/removeFarm/" +farmId)
				.retrieve()
				.bodyToMono(Boolean.class).block();
	}
	
	public Boolean deleteFarmer(String userName) {
		return webClient2.delete()
				.uri("/farmer/deletefarmer/" + userName)
				.retrieve()
				.bodyToMono(Boolean.class).block();

		
	}

}
