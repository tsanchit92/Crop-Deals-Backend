package farm.dto;

import java.util.ArrayList;
import java.util.List;

import farm.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartBillDto {

	List<CartItem> carttotalItems = new ArrayList<>();
	int totalBill;
}
