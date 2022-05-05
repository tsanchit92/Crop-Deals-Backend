package farm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import farm.dto.CartBillDto;
import farm.dto.CartItemDto;
import farm.model.CartItem;
import farm.model.Crop;
import farm.model.FarmModel;
import farm.model.OrderModel;
import farm.repository.CartItemRepository;
import farm.repository.FarmRepository;
import farm.repository.OrderRepository;
import reactor.core.publisher.Mono;

@Service
public class ShoppingcartItemService {

	@Autowired
	WebClient.Builder webCient;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	public OrderRepository orderRepo;

	@Autowired
	public FarmRepository farmRepository;

	public boolean addItems(CartItemDto cartItemDto) {

		int itemCost = cartItemDto.getQuantity() * cartItemDto.getPrice();

		FarmModel farmModel = farmRepository.findById(cartItemDto.getUserName()).get();

		CartItem cartItem = new CartItem(cartItemDto.getCropName(), cartItemDto.getCropType(), cartItemDto.getPrice(),
				cartItemDto.getId(), cartItemDto.getQuantity(), null, itemCost);

		farmModel.getCartItems().add(cartItem);
		cartItem.setFarmModel(farmModel);
		cartItemRepository.save(cartItem);
		farmModel.setTotalBill(farmModel.getTotalBill() + itemCost);

		farmRepository.save(farmModel);

		return true;

	}

	public boolean removeItems(CartItemDto cartItemDto) {
		FarmModel farmModel = farmRepository.findById(cartItemDto.getUserName()).get();

		for (CartItem icart : farmModel.getCartItems()) {
			if (icart.getId() == cartItemDto.getId()) {
				farmModel.getCartItems().remove(icart);
				cartItemRepository.delete(icart);
				farmModel.setTotalBill(farmModel.getTotalBill() - icart.getCost());
				farmRepository.save(farmModel);
				return true;
			}
		}
		return false;
	}

	public CartBillDto listCartItem(String userName) {

		FarmModel farmModel = farmRepository.findById(userName).get();
		CartBillDto cartBillDto = new CartBillDto(farmModel.getCartItems(), farmModel.getTotalBill());
		return cartBillDto;

	}

	public Boolean checkout(String userName) {
		
		
		HashMap<Integer,Integer> CropIds=new HashMap<>();
		FarmModel farmModel=farmRepository.findById(userName).get();
		for(CartItem icart : farmModel.getCartItems())
		{
			CropIds.put(icart.getCropId(), icart.getQuantity());
			OrderModel order =new OrderModel(new Date(System.currentTimeMillis()),icart.getCropName(),icart.getCropType(),
					icart.getCropPrice(),icart.getCropId(),icart.getQuantity(),icart.getCost(),farmModel);
			farmModel.getOrder().add(order);
			orderRepo.save(order);
			farmRepository.save(farmModel);
		}
		webCient.build().post().uri("http://Farmer/farmer/quantityManagement")
		.header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
		.body(Mono.just(CropIds),HashMap.class)
		.accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(boolean.class).block();
		
		List<CartItem> items = cartItemRepository.getCartItem(userName);
		ListIterator<CartItem>itr=items.listIterator();
		while(itr.hasNext())
		{
			CartItem c =itr.next();
			c.setFarmModel(null);
			cartItemRepository.deleteById(c.getId());
		}
		farmModel.setCartItems(new ArrayList<>());
		farmRepository.save(farmModel);
		
		return true;
	}

	public String pay() {
		return webCient.build().get().uri("http://localhost:8080").accept(MediaType.TEXT_HTML).retrieve()
				.bodyToMono(String.class).block();

	}

}
