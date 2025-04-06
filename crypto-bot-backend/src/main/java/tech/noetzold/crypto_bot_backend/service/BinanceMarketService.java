package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceMarketService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;
    private final BinanceProperties binanceProperties;

    public List getCandles(String symbol, String interval, int limit) {
        String url = binanceProperties.getApiUrl() + "/v3/klines?symbol=" + symbol + "&interval=" + interval + "&limit=" + limit;
        log.info("📊 [getCandles] Request URL: {}", url);

        return binanceWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(List.class)
                .doOnError(error -> log.error("❌ [getCandles] Error: {}", error.getMessage()))
                .block();
    }

    public Map getDepth(String symbol, int limit) {
        String url = binanceProperties.getApiUrl() + "/v3/depth?symbol=" + symbol + "&limit=" + limit;
        log.info("📉 [getDepth] Request URL: {}", url);

        return binanceWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> log.error("❌ [getDepth] Error: {}", error.getMessage()))
                .block();
    }

    public Map getLastPrice(String symbol) {
        String url = binanceProperties.getApiUrl() + "/v3/ticker/price?symbol=" + symbol;
        log.info("💰 [getLastPrice] Request URL: {}", url);

        return binanceWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> log.error("❌ [getLastPrice] Error: {}", error.getMessage()))
                .block();
    }

    public Map getBookTicker(String symbol) {
        String url = binanceProperties.getApiUrl() + "/v3/ticker/bookTicker?symbol=" + symbol;
        log.info("📘 [getBookTicker] Request URL: {}", url);

        return binanceWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> log.error("❌ [getBookTicker] Error: {}", error.getMessage()))
                .block();
    }

    public List<Map> getRecentTrades(String symbol, int limit) {
        String url = binanceProperties.getApiUrl() + "/v3/trades?symbol=" + symbol + "&limit=" + limit;
        log.info("📈 [getRecentTrades] Request URL: {}", url);

        return binanceWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Map.class)
                .doOnError(error -> log.error("❌ [getRecentTrades] Error: {}", error.getMessage()))
                .collectList()
                .block();
    }
}
