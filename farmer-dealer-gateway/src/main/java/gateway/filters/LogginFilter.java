package gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class LogginFilter implements GatewayFilter {

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		System.out.println("Filter");
		ServerHttpRequest request = exchange.getRequest();
		String url = request.getURI().getPath();
		if (request.getHeaders().containsKey("Authorization") == false)
		{

			if (url.equals("/authenticate"))
				// request forward to designated login microservice
				return chain.filter(exchange);
		}
		return chain.filter(exchange);
	}
	
}
		// else {
//			
//	Mono<Boolean> result = webClientBuilder.build().post().uri("http://login-service/validate")
//					.header(HttpHeaders.AUTHORIZATION).accept(MediaType.APPLICATION_JSON).retrieve()
//					.bodyToMono(Boolean.class);
//			Boolean res = result.toProcessor().block();
//		}
//		return null;
//
//	
	


