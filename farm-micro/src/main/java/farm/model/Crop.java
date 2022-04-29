package farm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Crop {
	
	public int id;
	public String cropName;
	public String cropType;
	public int cropQuantity;
	public int price;


}
