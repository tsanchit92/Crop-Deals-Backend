package farmer.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="soldCrops")
public class SoldCrops {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int soldId;
	public int quantitySold;
	public String cropName;
	public String cropType;
	public int cropPrice;
	public int cost;
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
	@JoinColumn(name = "userName",referencedColumnName = "userName")
	private FarmerModel farmer;
	
	public SoldCrops(int quantitySold, String cropName, String cropType, int cropPrice, int cost, FarmerModel farmer) {
		super();
		this.quantitySold = quantitySold;
		this.cropName = cropName;
		this.cropType = cropType;
		this.cropPrice = cropPrice;
		this.cost = cost;
		this.farmer = farmer;
	}
	

}
