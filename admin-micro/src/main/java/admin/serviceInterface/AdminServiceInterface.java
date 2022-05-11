package admin.serviceInterface;

import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;

import admin.model.FarmModel;
import admin.model.FarmerModel;
import admin.model.OrderModel;

public interface AdminServiceInterface {

	Boolean deleteFarm(String userName);

	Boolean deleteFarmer(String userName);

	List<FarmerModel> getAllFarmers();

	List<FarmModel> getAllFarm();

	List<OrderModel> getAllOrders();

	Boolean validateToken(ServerHttpRequest request);

}