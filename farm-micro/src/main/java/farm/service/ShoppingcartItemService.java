package farm.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import farm.dto.CartBillDto;
import farm.dto.CartItemDto;
import farm.model.CartItem;
import farm.model.FarmModel;
import farm.repository.CartItemRepository;
import farm.repository.FarmRepository;
import reactor.core.publisher.Mono;

@Service
public class ShoppingcartItemService {

	@Autowired
	WebClient.Builder webCient;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	public FarmRepository farmRepository;

	public boolean addItems(CartItemDto cartItemDto) {

		int itemCost = cartItemDto.getQuantity() * cartItemDto.getCrop().getPrice();

		FarmModel farmModel = farmRepository.findById(cartItemDto.getFarmId()).get();

		CartItem cartItem = new CartItem(cartItemDto.getId(), cartItemDto.getCrop().getCropName(),
				cartItemDto.getCrop().getCropType(), cartItemDto.getCrop().getPrice(), cartItemDto.getCrop().getId(),
				cartItemDto.getQuantity(), null, itemCost);

		farmModel.getCartItems().add(cartItem);
		cartItem.setFarmModel(farmModel);
		cartItemRepository.save(cartItem);

		for (CartItem icart : farmModel.getCartItems()) {
			farmModel.setTotalBill(farmModel.getTotalBill() + icart.getCost());
		}

		farmRepository.save(farmModel);

		return true;

	}

	public boolean removeItems(CartItemDto cartItemDto) {
		FarmModel farmModel = farmRepository.getById(cartItemDto.getFarmId());

		for (CartItem icart : farmModel.getCartItems()) {
			if (icart.getId() == cartItemDto.getId()) {
				farmModel.getCartItems().remove(icart);
				cartItemRepository.delete(icart);
				farmModel.setTotalBill(farmModel.getTotalBill() - icart.getCost());
				farmRepository.save(farmModel);

			}
		}

		return true;
	}

	public CartBillDto listCartItem(int farmId) {

		FarmModel farmModel = farmRepository.findById(farmId).get();
		CartBillDto cartBillDto = new CartBillDto(farmModel.getCartItems(), 			farmModel.getTotalBill());
		return cartBillDto;

	}

	public Boolean checkout(int farmId) {
		
		HashMap<Integer,Integer> CropIds=new HashMap<>();
		
		FarmModel farmModel=farmRepository.findById(farmId).get();
		for(CartItem icart : farmModel.getCartItems())
		{
			CropIds.put(icart.getCropId(), icart.getQuantity());
		}
		
		webCient.build().post().uri("http://farmer/farmer/quantityManagement")
		.header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
		.body(Mono.just(CropIds),ArrayList.class)
		.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(boolean.class).block();

		return true;
	}

}
