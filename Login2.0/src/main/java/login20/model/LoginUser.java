package login20.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("login-service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
	@Id
	private String userName;
	private String password;
	private String roles;
	private String Status;

}
