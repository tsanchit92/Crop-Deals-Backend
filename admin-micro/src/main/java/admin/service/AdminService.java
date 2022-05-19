package admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import admin.com.util.JwtUtil;
import admin.exception.AdminException;
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
//setting vase url for web client 
	WebClient webClient = WebClient.create("http://localhost:11000");
	WebClient webClient2 = WebClient.create("http://localhost:8000");

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AdminRepository adminrepo;

	@Override
	public Boolean deleteFarm(String userName)  {
//callinig method of farm service for deactivating dealer		
		log.info(userName);
			return webClient.delete().uri("/farm/removeFarm/" + userName)
					.retrieve().bodyToMono(Boolean.class).block();
	
	}

	@Override
	public Boolean deleteFarmer(String userName) {
		//callinig method of farmer service for deactivating farmer	
		log.info(userName);
			return webClient2.delete().uri("/farmer/deletefarmer/" + userName).retrieve().bodyToMono(Boolean.class)
					.block();
	}

	@Override
	public List<FarmerModel> getAllFarmers() {
		//callinig method of farmer service for getting list all  farmer	
			return webClient2.get().uri("/farmer/getAllFarmer").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<FarmerModel>>() {
					}).block();
			
	}

	@Override
	public List<FarmModel> getAllFarm() {
		//callinig method of farm service for getting list of all  dealer	
			return webClient.get().uri("/farm/getAll").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<FarmModel>>() {
					}).block();
		
	}

	@Override
	public List<OrderModel> getAllOrders() {
		//callinig method of farm service for getting list of all  order placed till now using this web site	
			return webClient.get().uri("/farm/getOrders").accept(MediaType.APPLICATION_JSON).retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<OrderModel>>() {
					}).block();
		
	}

	@Override
	public Boolean validateToken(ServerHttpRequest request) {
//method for validating token
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

			AdminModel farmModel = adminrepo.findById(username)
	.orElseThrow( ()->  new AdminException("admin not found",HttpStatus.BAD_REQUEST));
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
