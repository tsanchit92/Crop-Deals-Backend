package farm.model;

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
@Table(name="farm")
public class FarmModel {
	@Id
	public int id;
	public String name;
	public String userName;
	public String password;
	public int contact;
	public String email;
}
