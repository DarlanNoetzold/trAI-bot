package tech.noetzold.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth-api", r -> r.path("/api/auth/**")
						.uri("http://auth-api:8081"))
				.route("spot-api", r -> r.path("/api/account/**", "/api/market/**", "/api/trade/**")
						.uri("http://spot-api:8082"))
				.route("strategy-api", r -> r.path("/api/strategies/**", "/api/custom-strategies/**", "/api/logs/**")
						.uri("http://strategy-api:8083"))
				.route("futures-api", r -> r.path("/api/futures/**")
						.uri("http://futures-api:8084"))
				.route("scheduler-api", r -> r.path("/api/scheduler/**")
						.uri("http://scheduler-api:8086"))
				.build();
	}
}
