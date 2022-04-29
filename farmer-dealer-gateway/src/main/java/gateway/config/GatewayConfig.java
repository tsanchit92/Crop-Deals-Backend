package gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import gateway.filters.LogginFilter;

@Configuration
public class GatewayConfig {

	@Autowired
	LogginFilter filter;
	
	@Bean
		public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
			return builder.routes()
					
					.route(r->r.path("/authenticate")
					.uri("http://localhost:12000/"))
					
					
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
					.filters(f->f.filter(filter))
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
