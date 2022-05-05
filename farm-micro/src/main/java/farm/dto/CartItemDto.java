package farm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
	public int id;
	public String userName;
	public String cropName; 
	public String cropType;
	public int cropQuantity;
	public int price;
	public int quantity;

}
