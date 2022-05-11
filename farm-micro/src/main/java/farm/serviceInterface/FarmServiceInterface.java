package farm.serviceInterface;

import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;

import farm.dto.EditDto;
import farm.dto.OrderDto;
import farm.dto.RatingDto;
import farm.model.Address;
import farm.model.BankAccountDeatil;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.model.OrderModel;

public interface FarmServiceInterface {

	boolean register(FarmModel farmModel);

	List<Crop> getFarmers();

	void removeDealer(String userName);

	boolean editProfile(EditDto farm);

	boolean rateFarmer(RatingDto dto);

	void sendEmail();

	FarmModel getDetails(String userName);

	List<FarmModel> getAllFarm();

	List<OrderDto> getOrders();

	Address getAddress(int id);

	BankAccountDeatil getbankDetails(int id);

	List<OrderModel> getFarmOrders(String userName);

	Boolean validateToken(ServerHttpRequest request);

}