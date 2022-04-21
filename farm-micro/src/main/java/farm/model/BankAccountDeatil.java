package farm.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BankAccountDeatil {
	public String bankAccountHolderName;
	public int bankAccountNo;
	public String ifscCode;
	
	

}
