package farmer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bankDetails")
public class BankAccountDeatil {
	public String bankAccountHolderName;
	@Id
	public int bankAccountNo;
	public String ifscCode;
	
	

}
