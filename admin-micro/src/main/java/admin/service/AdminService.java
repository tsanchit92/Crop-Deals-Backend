package admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import admin.com.util.JwtUtil;
import admin.model.AdminModel;
import admin.model.FarmModel;
import admin.model.FarmerModel;
import admin.model.OrderModel;
import admin.repository.AdminRepository;
import admin.serviceInterface.AdminServiceInterface;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminService implements AdminServiceInterface {

	WebClient webClient = WebClient.create("http://localhost:11000");
	WebClient webClient2 = WebClient.create("http://localhost:8000");

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AdminRepository adminrepo;

	@Override
	public Boolean deleteFarm(String userName) {
		
		log.info(userName);
			return webClient.delete().uri("/farm/removeFarm/" + userName)
					.retrieve().bodyToMono(Boolean.class).block();
	}

	@Override
	public Boolean deleteFarmer(String userName) {
		
		log.info(userName);
			return webClient2.delete().uri("/farmer/deletefarmer/" + userName).retrieve().bodyToMono(Boolean.class)
					.block();
	}

	@Override
	public List<FarmerModel> getAllFarmers() {
		
			return webClient2.get().uri("/farmer/getAllFarmer").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<FarmerModel>>() {
					}).block();
			
	}

	@Override
	public List<FarmModel> getAllFarm() {
	
			return webClient.get().uri("/farm/getAll").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<FarmModel>>() {
					}).block();
		
	}

	@Override
	public List<OrderModel> getAllOrders() {

			return webClient.get().uri("/farm/getOrders").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<OrderModel>>() {
					}).block();
		
	}

	@Override
	public Boolean validateToken(ServerHttpRequest request) {

			log.info("validating token");
		String authorizationHeader = request.getHeaders().getFirst("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring("Bearer ".length());
			username = jwtUtil.extractUsername(jwt);
		} else
			return false;

		if (username != null) {

			AdminModel farmModel = adminrepo.findById(username).get();
			if (farmModel != null) {
				if (jwtUtil.validateToken(jwt, farmModel.getUserName())) {

					return true;

				} else
					return false;
			} else
				return false;
		} else
			return false;
	}

}
