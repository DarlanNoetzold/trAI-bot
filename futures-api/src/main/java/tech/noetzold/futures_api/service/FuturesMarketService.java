package tech.noetzold.futures_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.futures_api.util.BinanceSignatureUtil;
import tech.noetzold.futures_api.util.TimeUtil;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesMarketService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;
    private final TimeUtil timeUtil;

    public Map<String, Object> getPrice(String symbol) {
        String url = "/v1/ticker/price?symbol=" + symbol;
        log.info("[getPrice] Requesting: {}", url);

        try {
            return (Map<String, Object>) binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        } catch (Exception e) {
            log.error("[getPrice] Error: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    public Object getCandlesticks(String symbol, String interval, int limit) {
        String url = "/v1/klines?symbol=" + symbol + "&interval=" + interval + "&limit=" + limit;
        log.info("[getCandlesticks] URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        } catch (Exception e) {
            log.error("[getCandlesticks] Error: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
