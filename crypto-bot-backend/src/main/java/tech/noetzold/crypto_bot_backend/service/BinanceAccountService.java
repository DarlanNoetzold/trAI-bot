package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;
import tech.noetzold.crypto_bot_backend.util.BinanceSignatureUtil;
import tech.noetzold.crypto_bot_backend.util.TimeUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceAccountService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;

    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    public Map getAccountInfo() {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getSecretKey());

        String fullUrl = binanceProperties.getApiUrl() + "/v3/account?" + query + "&signature=" + signature;

        log.info("🌐 [getAccountInfo] Base URL: {}", binanceProperties.getApiUrl());
        log.info("🔗 [getAccountInfo] Full request URL: {}", fullUrl);
        log.info("🔐 [getAccountInfo] Generated signature: {}", signature);

        return binanceWebClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(error -> log.error("❌ [getAccountInfo] Request error: {}", error.getMessage()))
                .block();
    }

    public List<Map> getAccountTrades(String symbol, Integer limit) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (limit != null) params.put("limit", limit.toString());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        String fullUrl = binanceProperties.getApiUrl() + "/v3/myTrades?" + query;

        log.info("🔗 [getAccountTrades] Full request URL: {}", fullUrl);

        return binanceWebClient.get()
                .uri(fullUrl)
                .retrieve()
                .bodyToFlux(Map.class)
                .doOnError(error -> log.error("❌ [getAccountTrades] Request error: {}", error.getMessage()))
                .collectList()
                .block();
    }

    private String buildQuery(Map<String, String> params) {
        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getSecretKey());
        log.info("🧮 [buildQuery] Raw query: {}", query);
        log.info("🔐 [buildQuery] Generated signature: {}", signature);
        return query + "&signature=" + signature;
    }
}
