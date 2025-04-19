package tech.noetzold.spot_api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import tech.noetzold.spot_api.context.BinanceEnvironmentContext;
import tech.noetzold.spot_api.enums.BinanceEnvironment;

import java.io.IOException;

@Component
public class BinanceEnvironmentFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String envHeader = ((HttpServletRequest) request).getHeader("X-BINANCE-ENV");
            BinanceEnvironment environment = "PRODUCTION".equalsIgnoreCase(envHeader)
                    ? BinanceEnvironment.PRODUCTION
                    : BinanceEnvironment.TESTNET;

            BinanceEnvironmentContext.set(environment);
            chain.doFilter(request, response);
        } finally {
            BinanceEnvironmentContext.clear();
        }
    }
}
