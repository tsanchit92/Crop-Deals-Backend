package farm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import farm.dto.CartBillDto;
import farm.dto.CartItemDto;
import farm.service.ShoppingcartItemService;

@CrossOrigin
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

	@Autowired
	public ShoppingcartItemService shoppingcartItemService;

	@GetMapping("/getCart/{userName}")
	public CartBillDto getCart(@PathVariable String userName) {
		return shoppingcartItemService.listCartItem(userName);

	}

	@PostMapping("/addItems")
	public boolean addItems(@RequestBody CartItemDto cartItemDto) {
		return shoppingcartItemService.addItems(cartItemDto);
	}

	@DeleteMapping("/deleteItems")
	public boolean removeItems(@RequestBody CartItemDto cartItemDto) {
		return shoppingcartItemService.removeItems(cartItemDto);
	}
	
	@GetMapping("/checkOut/{userName}")
	public Boolean checkout(@PathVariable String userName)
			{
				return shoppingcartItemService.checkout(userName);
			}
	@GetMapping("/pay")
	public String pay()
			{
				return shoppingcartItemService.pay();
			}

}
