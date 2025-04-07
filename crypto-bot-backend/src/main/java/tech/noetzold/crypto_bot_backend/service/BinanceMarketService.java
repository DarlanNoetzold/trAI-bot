package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceMarketService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;
    private final BinanceProperties binanceProperties;

    private boolean isSymbolValid(String symbol) {
        return symbol != null && !symbol.trim().isEmpty() && symbol.matches("[A-Z0-9]+");
    }

    public List<List<Object>> getCandles(String symbol, String interval, int limit) {
        if (!isSymbolValid(symbol)) {
            log.warn("⚠️ [getCandles] Símbolo inválido: {}", symbol);
            return Collections.emptyList();
        }

        String url = "/v3/klines?symbol=" + symbol + "&interval=" + interval + "&limit=" + limit;
        log.info("📊 [getCandles] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<List<List<Object>>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("❌ [getCandles] WebClient error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("❌ [getCandles] General error: {}", e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    public Map<String, Object> getDepth(String symbol, int limit) {
        if (!isSymbolValid(symbol)) {
            log.warn("⚠️ [getDepth] Símbolo inválido: {}", symbol);
            return Collections.emptyMap();
        }

        String url = "/v3/depth?symbol=" + symbol + "&limit=" + limit;
        log.info("📉 [getDepth] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("❌ [getDepth] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyMap();
    }

    public Map<String, Object> getLastPrice(String symbol) {
        if (!isSymbolValid(symbol)) {
            log.warn("⚠️ [getLastPrice] Símbolo inválido: {}", symbol);
            return Collections.emptyMap();
        }

        String url = "/v3/ticker/price?symbol=" + symbol;
        log.info("💰 [getLastPrice] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("❌ [getLastPrice] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyMap();
    }

    public Map<String, Object> getBookTicker(String symbol) {
        if (!isSymbolValid(symbol)) {
            log.warn("⚠️ [getBookTicker] Símbolo inválido: {}", symbol);
            return Collections.emptyMap();
        }

        String url = "/v3/ticker/bookTicker?symbol=" + symbol;
        log.info("📘 [getBookTicker] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("❌ [getBookTicker] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyMap();
    }

    public List<Map<String, Object>> getRecentTrades(String symbol, int limit) {
        if (!isSymbolValid(symbol)) {
            log.warn("⚠️ [getRecentTrades] Símbolo inválido: {}", symbol);
            return Collections.emptyList();
        }

        String url = "/v3/trades?symbol=" + symbol + "&limit=" + limit;
        log.info("📈 [getRecentTrades] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("❌ [getRecentTrades] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyList();
    }
}
