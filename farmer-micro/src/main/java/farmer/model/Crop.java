package farmer.model;

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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="crop")
public class Crop {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int id;
	public Crop(String cropName, String cropType, int cropQuantity, int price, FarmerModel farmer) {
		super();
		this.cropName = cropName;
		this.cropType = cropType;
		this.cropQuantity = cropQuantity;
		this.price = price;
		this.farmer = farmer;
	}
	public String cropName;
	public String cropType;
	public int cropQuantity;
	public int price;
	@JsonIgnore
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "userName",referencedColumnName = "userName")
	private FarmerModel farmer;
}
