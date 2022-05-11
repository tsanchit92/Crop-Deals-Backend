package admin.com.util;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class JwtUtil {

	private String SECRET_KEY = "secret";

	public String extractUsername(String token) {
		log.info("userName extracted from token");
		return extractClaim(token, Claims::getSubject);
		
	}

	public Date extractExpiration(String token) {
		
		log.info("expiration of token is extracted from token ");
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		log.info("claims extraction taking place");
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		log.info("for parsing of jwt and getting body decrypted");
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		log.info("checking if token is expired or not");
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, String userName) {
		log.info("Validating token");
		final String username = extractUsername(token);
		return (username.equals(userName) && !isTokenExpired(token));
	}
}
