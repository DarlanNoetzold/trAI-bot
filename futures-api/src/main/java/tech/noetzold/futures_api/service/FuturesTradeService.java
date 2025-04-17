package tech.noetzold.futures_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.futures_api.config.BinanceProperties;
import tech.noetzold.futures_api.dto.FuturesOrderDTO;
import tech.noetzold.futures_api.util.BinanceSignatureUtil;
import tech.noetzold.futures_api.util.TimeUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuturesTradeService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;

    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    public Map<String, Object> createOrder(FuturesOrderDTO order) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", order.getSymbol());
        params.put("side", order.getSide());
        params.put("type", order.getType());
        params.put("quantity", String.valueOf(order.getQuantity()));
        if (order.getPrice() != null) {
            params.put("price", String.valueOf(order.getPrice()));
        }
        if (order.getTimeInForce() != null) {
            params.put("timeInForce", order.getTimeInForce());
        }
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        String url = "/fapi/v1/order?" + query;

        try {
            return binanceWebClient.post()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao criar ordem: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    public Map<String, Object> cancelOrder(String symbol, Long orderId) {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "symbol=" + symbol + "&orderId=" + orderId + "&timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());
        String url = "/fapi/v1/order?" + query + "&signature=" + signature;

        try {
            return binanceWebClient.delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao cancelar ordem: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private String buildQuery(Map<String, String> params) {
        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());
        return query + "&signature=" + signature;
    }
}

