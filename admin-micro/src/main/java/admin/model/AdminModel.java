package admin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("admin-service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminModel {
	@Id
	public String userName;
	public String password;
	

}
