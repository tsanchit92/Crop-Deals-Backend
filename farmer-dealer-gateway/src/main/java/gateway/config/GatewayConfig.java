package gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Bean
		public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
			return builder.routes()
					
					.route(r -> r.path("/farmer/getCrops")
					.uri("http://localhost:8000/"))
					
					.route(r -> r.path("/farmer/deletefarmer")
					.uri("http://localhost:8000/"))
					
					.route(r -> r.path("/farmer/register")
					.uri("http://localhost:8000/"))
							
					.route(r -> r.path("/farmer/addCrop")
					.uri("http://localhost:8000/"))
					
					.route(r -> r.path("/farmer/removeCrop")
					.uri("http://localhost:8000/"))
							
					.route(r -> r.path("/farmer/editProfile")
					.uri("http://localhost:8000/"))
									
					.route(r -> r.path("/farmer/rating")
					.uri("http://localhost:8000/"))
					
					.route(r -> r.path("/farm/register")
					.uri("http://localhost:11000/"))
					
					.route(r -> r.path("/farm/getCrops")
					.uri("http://localhost:11000/"))
					
					.route(r -> r.path("/farmer/removeFarm")
					.uri("http://localhost:11000/"))
					
					.route(r -> r.path("/admin/register")
					.uri("http://localhost:9000/"))
					
					.route(r -> r.path("/admin/removeFarmer")
					.uri("http://localhost:9000/"))
					
					.route(r -> r.path("/admin/removeFarm")
					.uri("http://localhost:9000/"))
					
					.build();
					
							
			
		}

}
