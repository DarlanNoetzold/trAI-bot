package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;
import tech.noetzold.crypto_bot_backend.util.BinanceSignatureUtil;
import tech.noetzold.crypto_bot_backend.util.TimeUtil;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceAccountService {

    private final WebClient binanceWebClient;
    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    public Map getAccountInfo() {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getSecretKey());
        URI uri = UriComponentsBuilder.fromPath("/v3/account")
                .query(query + "&signature=" + signature)
                .build(true)
                .toUri();

        return binanceWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public List<Map> getAccountTrades(String symbol, Integer limit) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (limit != null) params.put("limit", limit.toString());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/myTrades", query);

        return binanceWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    private String buildQuery(Map<String, String> params) {
        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getSecretKey());
        return query + "&signature=" + signature;
    }

    private URI buildSignedUri(String path, String query) {
        return UriComponentsBuilder.fromPath(path)
                .query(query)
                .build(true)
                .toUri();
    }
}
