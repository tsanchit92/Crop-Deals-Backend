
package farmer.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter

@Getter

@AllArgsConstructor

@NoArgsConstructor
public class FarmerExceptionResponse {

	HttpStatus status;
	String message;
	LocalDateTime date;

}
