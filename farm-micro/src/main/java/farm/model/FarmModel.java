package farm.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	

	public String name;
	@Id
	public String userName;
	public String password;
	public long contact;
	public String email;
	public int totalBill;
	public String status;
	@JsonIgnore
	@OneToMany(mappedBy = "farmModel", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,
			CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	public List<CartItem> cartItems = new ArrayList<>();
	@JsonIgnore
	@OneToMany(mappedBy = "farmModel", cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,
			CascadeType.REFRESH},fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	public List<OrderModel> Order = new ArrayList<>();
	
}
