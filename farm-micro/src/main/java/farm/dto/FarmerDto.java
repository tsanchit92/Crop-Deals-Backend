package farm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FarmerDto {
	
	
	public String firstName;
	public String lastName;
	public String email;
	public int contact;
	public String userName;
	public String password;
	public int rating;
	public String houseNo;
	public String locality;
	public String town;
	public String district;
	public String State;
	public int postalCode;
	public int id;
	public String cropName;
	public String cropType;
	public String cropQuantity;
	public String price;
	public int bankAccountNo;
	public String ifscCode;
	public String bankAccountHolderName;

	
}