package tech.noetzold.api_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

	@Value("${gateway.routes.auth}")
	private String authApi;

	@Value("${gateway.routes.spot}")
	private String spotApi;

	@Value("${gateway.routes.strategy}")
	private String strategyApi;

	@Value("${gateway.routes.futures}")
	private String futuresApi;

	@Value("${gateway.routes.scheduler}")
	private String schedulerApi;

	@Value("${gateway.routes.payment}")
	private String paymentApi;

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth-api", r -> r.path("/api/auth/**").uri(authApi))
				.route("spot-api", r -> r.path("/api/account/**", "/api/market/**", "/api/trade/**").uri(spotApi))
				.route("strategy-api", r -> r.path("/api/strategies/**", "/api/custom-strategies/**", "/api/logs/**").uri(strategyApi))
				.route("futures-api", r -> r.path("/api/futures/**").uri(futuresApi))
				.route("scheduler-api", r -> r.path("/api/scheduler/**").uri(schedulerApi))
				.route("payment-api", r -> r.path("/api/payment/**").uri(paymentApi))
				.build();
	}
}
