package farmer.serviceInterface;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.server.reactive.ServerHttpRequest;

import farmer.dto.CropDto;
import farmer.dto.EditDto;
import farmer.dto.FarmerDto;
import farmer.dto.RatingDto;
import farmer.model.Address;
import farmer.model.BankAccountDeatil;
import farmer.model.Crop;
import farmer.model.FarmerModel;

public interface FarmerServiceInterface {

	boolean register(FarmerDto farmerDto);

	boolean addCrop(CropDto crop, ServerHttpRequest request);

	boolean removeCrop(String userName, Integer id, ServerHttpRequest request);

	List<Crop> getFarmerCrops();

	void removeFamer(String userName);

	boolean rateFarmer(RatingDto dto);

	boolean editProfile(EditDto dto, ServerHttpRequest request);

	Address getAddress(int id);

	BankAccountDeatil getbankDetails(int id);

	Boolean quantityManagement(HashMap<Integer, Integer> CropIds);

	Boolean saveFarmEmail(String Emails);

	FarmerDto getFarmerDetails(String userName, ServerHttpRequest request);

	List<Crop> getFarmerCrops(String userName, ServerHttpRequest request);

	List<FarmerModel> getFarmers();

	Boolean validateToken(ServerHttpRequest request);

}