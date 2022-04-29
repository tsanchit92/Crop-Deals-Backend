package farm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "farm")
public class FarmModel {
	@Id
	public int farmId;
	public String name;
	public String userName;
	public String password;
	public int contact;
	public String email;
	public int totalBill;
	public String status;
	@JsonIgnore
	@OneToMany(mappedBy = "farmModel", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,
			CascadeType.REFRESH},fetch = FetchType.EAGER)
	public List<CartItem> cartItems = new ArrayList<>();
	
}
