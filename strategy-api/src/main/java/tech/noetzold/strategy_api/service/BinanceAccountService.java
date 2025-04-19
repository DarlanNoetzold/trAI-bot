package tech.noetzold.strategy_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tech.noetzold.strategy_api.config.BinanceProperties;
import tech.noetzold.strategy_api.context.BinanceEnvironmentContext;
import tech.noetzold.strategy_api.util.BinanceSignatureUtil;
import tech.noetzold.strategy_api.util.TimeUtil;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceAccountService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;

    private final BinanceProperties binanceProperties;
    private final tech.noetzold.strategy_api.context.BinanceEnvironmentContext environmentContext;
    private final TimeUtil timeUtil;

    public Map<String, Object> getAccountInfo() {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());

        String url = "/v3/account?" + query + "&signature=" + signature;
        log.info("🌐 [getAccountInfo] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("❌ [getAccountInfo] WebClient error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("❌ [getAccountInfo] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyMap();
    }

    public List<Map<String, Object>> getAccountTrades(String symbol, Integer limit) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (limit != null) params.put("limit", limit.toString());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        String url = "/v3/myTrades?" + query;
        log.info("🔗 [getAccountTrades] Request URL: {}", url);

        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class).map(msg -> new RuntimeException("Erro Binance API: " + msg)))
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            log.error("❌ [getAccountTrades] WebClient error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("❌ [getAccountTrades] Error: {}", e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    private String buildQuery(Map<String, String> params) {
        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());
        log.info("🧮 [buildQuery] Raw query: {}", query);
        log.info("🔐 [buildQuery] Generated signature: {}", signature);
        return query + "&signature=" + signature;
    }
}
