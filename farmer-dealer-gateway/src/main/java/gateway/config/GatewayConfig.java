package gateway.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import gateway.filters.LogginFilter;

@CrossOrigin
@Configuration
public class GatewayConfig {

	@Autowired
	LogginFilter filter;

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()

				.route(r -> r.path("/authenticate").uri("http://localhost:12000/"))

				.route(r -> r.path("/farmer/**").uri("http://localhost:8000/"))

				.route(r -> r.path("/farm/**").uri("http://localhost:11000/"))
				
				.route(r -> r.path("/admin/**").uri("http://localhost:13000/"))

				
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

}
