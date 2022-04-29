package farm.dto;

import farm.model.Crop;
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
	public int farmId;
	public Crop crop; 
	public int quantity;

}
