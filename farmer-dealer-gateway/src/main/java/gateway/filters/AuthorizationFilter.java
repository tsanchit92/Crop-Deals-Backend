package gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import gateway.model.RouterValidator;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationFilter implements GatewayFilter {

	@Autowired
	private RouterValidator routerValidator;// custom route validator

	@Value("${jwt.secret}")
	private String secret;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();

		if (routerValidator.isSecured.test(request) && isAuthMissing(request) == false) {
			try {
				String authorizationHeader = getAuthHeader(request);
				String jwt = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
				JWTVerifier verifier = JWT.require(algorithm).build();
				verifier.verify(jwt);
			} 
			catch (AlgorithmMismatchException e)
			{
				onError(exchange,"Invalid Token: Algorithm mismatched", HttpStatus.FORBIDDEN);
			}
			catch (SignatureVerificationException  e)
			{
				onError(exchange,"Invalid Token: ignature not verified", HttpStatus.FORBIDDEN);
			}
			catch (TokenExpiredException  e)
			{
				onError(exchange,"Expired ", HttpStatus.FORBIDDEN);
			}
			catch (InvalidClaimException   e)
			{
				onError(exchange," Invalid claim", HttpStatus.FORBIDDEN);
			}
			catch (JWTVerificationException   e)
			{
				onError(exchange,"Expired or Invalid Token", HttpStatus.FORBIDDEN);
			}
		}
		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private String getAuthHeader(ServerHttpRequest request) {
		return request.getHeaders().getOrEmpty("Authorization").get(0);
	}

	private boolean isAuthMissing(ServerHttpRequest request) {
		return !request.getHeaders().containsKey("Authorization");
	}

}
