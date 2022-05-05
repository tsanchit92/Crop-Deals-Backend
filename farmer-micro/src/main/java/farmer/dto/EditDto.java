package farmer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditDto {
	

	public String firstName;
	public String lastName;
	public String email;
	public long contact;
	public String userName;
	public String password;
	public String houseNo;
	public String locality;
	public String town;
	public String district;
	public String State;
	public int postalCode;
	public long bankAccountNo;
	public String ifscCode;
	public String bankAccountHolderName;

}
