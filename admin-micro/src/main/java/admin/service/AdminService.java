package admin.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import admin.model.FarmModel;
import admin.model.FarmerModel;
import admin.model.OrderModel;

@Service
public class AdminService {

	WebClient webClient = WebClient.create("http://localhost:11000");
	WebClient webClient2 = WebClient.create("http://localhost:8000");

	public Boolean deleteFarm(String userName) {

		return webClient.delete().uri("/farm/removeFarm/" + userName).retrieve().bodyToMono(Boolean.class).block();
	}

	public Boolean deleteFarmer(String userName) {
		return webClient2.delete().uri("/farmer/deletefarmer/" + userName).retrieve().bodyToMono(Boolean.class).block();

	}

	public List<FarmerModel> getAllFarmers() {

		return webClient2.get().uri("/farmer/getAllFarmer").accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<FarmerModel>>() {
				}).block();

	}

	public List<FarmModel> getAllFarm() {

		return webClient.get().uri("/farm/getAll").accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<FarmModel>>() {
				}).block();

	}

	public List<OrderModel> getAllOrders() {
		return webClient.get().uri("/farm/getOrders").accept(MediaType.APPLICATION_JSON).retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<OrderModel>>() {
				}).block();
		
	}

}
