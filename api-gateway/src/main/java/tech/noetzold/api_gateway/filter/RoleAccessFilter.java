package tech.noetzold.api_gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
public class RoleAccessFilter implements GlobalFilter, Ordered {

    // Regras por prefixo de path
    private static final Map<String, Set<String>> ROLE_ACCESS = Map.of(
            "/api/market/**", Set.of("VIEWER", "TRADER", "ADMIN"),
            "/api/account/**", Set.of("VIEWER", "TRADER", "ADMIN"),
            "/api/trade/**", Set.of("TRADER", "ADMIN"),
            "/api/strategies/**", Set.of("TRADER", "ADMIN"),
            "/api/custom-strategies/**", Set.of("TRADER", "ADMIN"),
            "/api/scheduler/**", Set.of("TRADER", "ADMIN"),
            "/api/futures/**", Set.of("ADMIN")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        String role = exchange.getRequest().getHeaders().getFirst("X-ROLE");

        for (Map.Entry<String, Set<String>> entry : ROLE_ACCESS.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                if (role == null || !entry.getValue().contains(role)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                break;
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0; // Executa após JwtAuthFilter (-1)
    }
}
