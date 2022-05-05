package farm.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Orders")
public class OrderModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int OrderId;
	public Date date;
	public OrderModel(Date date,String cropName, String cropType, int cropPrice, int cropId, int quantity, int cost,
			FarmModel farmModel) {
		super();
		this.date=date;
		this.cropName = cropName;
		this.cropType = cropType;
		this.cropPrice = cropPrice;
		this.cropId = cropId;
		this.quantity = quantity;
		this.cost = cost;
		this.farmModel = farmModel;
	}
	public String cropName;
	public String cropType;
	public int cropPrice;
	public int  cropId;
	private int quantity;
	private  int cost;
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name = "userName",referencedColumnName = "userName")
	private FarmModel farmModel;
}
