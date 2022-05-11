package farm.serviceInterface;

import farm.dto.CartBillDto;
import farm.dto.CartItemDto;

public interface ShoppingCartServiceInterface {

	boolean addItems(CartItemDto cartItemDto);

	boolean removeItems(CartItemDto cartItemDto);

	CartBillDto listCartItem(String userName);

	Boolean checkout(String userName);

}