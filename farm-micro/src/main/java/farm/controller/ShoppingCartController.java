package farm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farm.dto.CartBillDto;
import farm.dto.CartItemDto;
import farm.model.CartItem;
import farm.service.ShoppingcartItemService;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	public ShoppingcartItemService shoppingcartItemService;

	@GetMapping("/getCart/{farmId}")
	public CartBillDto getCart(@PathVariable int farmId) {
		return shoppingcartItemService.listCartItem(farmId);

	}

	@PostMapping("/addItems")
	public boolean addItems(@RequestBody CartItemDto cartItemDto) {
		return shoppingcartItemService.addItems(cartItemDto);
	}

	@DeleteMapping("/deleteItems")
	public boolean removeItems(@RequestBody CartItemDto cartItemDto) {
		return shoppingcartItemService.removeItems(cartItemDto);
	}
	
	@GetMapping("/checkOut")
	public Boolean checkout(int farmId)
			{
				return shoppingcartItemService.checkout(farmId);
			}

}
