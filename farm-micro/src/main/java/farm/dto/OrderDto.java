package farm.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
	
	public String userName;
	public int OrderId;
	public Date date;
	public String cropName;
	public String cropType;
	public int cropPrice;
	public int  cropId;
	private int quantity;
	private  int cost;

}
