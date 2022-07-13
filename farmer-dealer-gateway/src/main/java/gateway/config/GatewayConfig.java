package gateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import gateway.filters.AuthorizationFilter;

@CrossOrigin
@Configuration
public class GatewayConfig {

	@Autowired
	AuthorizationFilter filter;

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("Farmer", r -> r.path("/farmer/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:8000"))

				.route("Admin", r -> r.path("/admin/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:13000"))
				

				.route("Farm", r -> r.path("/farm/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:11000"))
				
				.route("Login", r -> r.path("/authenticate")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:12000"))

				.route("Login", r -> r.path("/refreshToken")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:12000"))
				
				.route("Login", r -> r.path("/check")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:12000"))
				
				.build();
	}

	@Bean
	CorsWebFilter corsWebFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsWebFilter(source);
	}

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

}
