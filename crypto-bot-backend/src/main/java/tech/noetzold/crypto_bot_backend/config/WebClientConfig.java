package tech.noetzold.crypto_bot_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final BinanceProperties binanceProperties;

    @Bean
    public WebClient binanceWebClient() {
        return WebClient.builder()
                .baseUrl(binanceProperties.getApiUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .filter(apiKeyHeaderFilter())
                .build();
    }

    private ExchangeFilterFunction apiKeyHeaderFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ClientRequest modifiedRequest = ClientRequest.from(clientRequest)
                    .headers(headers -> headers.set("X-MBX-APIKEY", binanceProperties.getApiKey()))
                    .build();
            return reactor.core.publisher.Mono.just(modifiedRequest);
        });
    }
}