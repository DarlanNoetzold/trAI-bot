package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;
import tech.noetzold.crypto_bot_backend.dto.OrderOcoRequestDTO;
import tech.noetzold.crypto_bot_backend.dto.OrderRequestDTO;
import tech.noetzold.crypto_bot_backend.util.BinanceSignatureUtil;
import tech.noetzold.crypto_bot_backend.util.TimeUtil;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceOrderService {

    private final WebClient binanceWebClient;
    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    public Map placeOrder(OrderRequestDTO request) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", request.getSymbol());
        params.put("side", request.getSide());
        params.put("type", request.getType());
        if (request.getQuantity() != null)
            params.put("quantity", request.getQuantity().toString());
        if (request.getPrice() != null)
            params.put("price", request.getPrice().toString());
        if (request.getTimeInForce() != null)
            params.put("timeInForce", request.getTimeInForce());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/order", query);

        return binanceWebClient.post()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map placeOcoOrder(OrderOcoRequestDTO request) {
        long timestamp = timeUtil.getServerTimestamp();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", request.getSymbol());
        params.put("side", request.getSide());
        params.put("quantity", request.getQuantity().toString());
        params.put("price", request.getPrice().toString());
        params.put("stopPrice", request.getStopPrice().toString());
        params.put("stopLimitPrice", request.getStopLimitPrice().toString());
        params.put("timeInForce", request.getTimeInForce());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/order/oco", query);

        return binanceWebClient.post()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map cancelOrder(String symbol, Long orderId, String origClientOrderId) {
        long timestamp = timeUtil.getServerTimestamp();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (orderId != null) params.put("orderId", String.valueOf(orderId));
        if (origClientOrderId != null) params.put("origClientOrderId", origClientOrderId);
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/order", query);

        return binanceWebClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map cancelOcoOrder(String symbol, Long orderListId, String listClientOrderId) {
        long timestamp = timeUtil.getServerTimestamp();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (orderListId != null) params.put("orderListId", String.valueOf(orderListId));
        if (listClientOrderId != null) params.put("listClientOrderId", listClientOrderId);
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/orderList", query);

        return binanceWebClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public List<Map> getOpenOrders(String symbol) {
        long timestamp = timeUtil.getServerTimestamp();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/openOrders", query);

        return binanceWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();
    }

    public List<Map> getAllOrders(String symbol, Long startTime, Long endTime) {
        long timestamp = timeUtil.getServerTimestamp();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (startTime != null) params.put("startTime", startTime.toString());
        if (endTime != null) params.put("endTime", endTime.toString());
        params.put("timestamp", String.valueOf(timestamp));

        String query = buildQuery(params);
        URI uri = buildSignedUri("/v3/allOrders", query);

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
