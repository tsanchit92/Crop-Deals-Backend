package farmer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="farmer")
public class FarmerModel {
	
	public FarmerModel(String firstName, String lastName, String email, long contact, String status, String userName,
			String password, Address address, List<Crop> crops, BankAccountDeatil bankAccountDeatil, int rating) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contact = contact;
		this.status = status;
		this.userName = userName;
		this.password = password;
		this.address = address;
		this.crops = crops;
		this.bankAccountDeatil = bankAccountDeatil;
		this.rating = rating;
	}
	public String firstName;
	public String lastName;
	public String email;
	public long contact;
	public String status;
	@Id
//	@JsonIgnore
	public String userName;
	@JsonIgnore
	public String password;
	/* @JsonIgnore */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="addressId",referencedColumnName = "houseNo")
	@LazyCollection(LazyCollectionOption.FALSE)
	public Address address;
	@JsonIgnore
	@OneToMany(mappedBy = "farmer",cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH},fetch = FetchType.LAZY)
	public List<Crop> crops=new ArrayList<>();
	/* @JsonIgnore */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "bankAccountNo",referencedColumnName = "bankAccountNo")
	@LazyCollection(LazyCollectionOption.FALSE)
	public BankAccountDeatil bankAccountDeatil;
	public int rating;
	@OneToMany(mappedBy = "farmer",cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH},fetch = FetchType.LAZY)
	public List<SoldCrops> soldCrops=new ArrayList<>();
	
	 
	 

}
