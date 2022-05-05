package admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FarmModel {
	

	public String name;
	public String userName;
	public String password;
	public long contact;
	public String email;
	public int totalBill;
	public String status;
	
	
}
