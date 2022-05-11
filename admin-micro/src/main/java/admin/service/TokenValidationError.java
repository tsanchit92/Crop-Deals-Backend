package admin.service;



import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class TokenValidationError extends RuntimeException {
	
	String message ;
	HttpStatus status;
	

}
