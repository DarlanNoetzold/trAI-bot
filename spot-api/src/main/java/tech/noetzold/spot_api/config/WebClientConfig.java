package tech.noetzold.spot_api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.spot_api.context.BinanceEnvironmentContext;
import tech.noetzold.spot_api.enums.BinanceEnvironment;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
@ComponentScan("tech.noetzold.spot_api")
public class WebClientConfig {

    private final BinanceProperties binanceProperties;

    @Bean
    public WebClient binanceWebClient() {
        return WebClient.builder()
                .filter(rewriteUriBasedOnEnv())
                .filter(addApiKeyHeader())
                .build();
    }

    private ExchangeFilterFunction rewriteUriBasedOnEnv() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            String baseUrl = BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                    ? binanceProperties.getProductionApiUrl()
                    : binanceProperties.getTestnetApiUrl();

            String originalPath = request.url().getRawPath(); // mantém a path correta
            String originalQuery = request.url().getRawQuery();

            String finalUrl = baseUrl + originalPath;
            if (originalQuery != null && !originalQuery.isEmpty()) {
                finalUrl += "?" + originalQuery;
            }

            ClientRequest newRequest = ClientRequest.from(request)
                    .url(URI.create(finalUrl))
                    .build();

            return reactor.core.publisher.Mono.just(newRequest);
        });
    }

    private ExchangeFilterFunction addApiKeyHeader() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            String apiKey = BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                    ? binanceProperties.getProductionApiKey()
                    : binanceProperties.getTestnetApiKey();

            ClientRequest newRequest = ClientRequest.from(request)
                    .headers(headers -> headers.set("X-MBX-APIKEY", apiKey))
                    .build();

            return reactor.core.publisher.Mono.just(newRequest);
        });
    }
}
