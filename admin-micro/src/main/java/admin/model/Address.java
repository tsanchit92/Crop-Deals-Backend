package admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Address {

	public String houseNo;
	public String locality;
	public String town;
	public String district;
	public String State;
	public int postalCode;

}
