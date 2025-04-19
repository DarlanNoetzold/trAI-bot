package tech.noetzold.spot_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.spot_api.config.BinanceProperties;
import tech.noetzold.spot_api.context.BinanceEnvironmentContext;
import tech.noetzold.spot_api.dto.OrderOcoRequestDTO;
import tech.noetzold.spot_api.dto.OrderRequestDTO;
import tech.noetzold.spot_api.enums.BinanceEnvironment;
import tech.noetzold.spot_api.util.BinanceSignatureUtil;
import tech.noetzold.spot_api.util.TimeUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceOrderService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;

    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    private String getBaseUrl() {
        return BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                ? binanceProperties.getProductionApiUrl()
                : binanceProperties.getTestnetApiUrl();
    }

    public Map<String, Object> placeOrder(OrderRequestDTO request) {
        return performSignedPost("/v3/order", buildOrderParams(request));
    }

    public Map<String, Object> placeOcoOrder(OrderOcoRequestDTO request) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", request.getSymbol());
        params.put("side", request.getSide());
        params.put("quantity", request.getQuantity().toString());
        params.put("price", request.getPrice().toString());
        params.put("stopPrice", request.getStopPrice().toString());
        params.put("stopLimitPrice", request.getStopLimitPrice().toString());
        params.put("timeInForce", request.getTimeInForce());
        return performSignedPost("/v3/order/oco", params);
    }

    public Map<String, Object> cancelOrder(String symbol, Long orderId, String origClientOrderId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (orderId != null) params.put("orderId", orderId.toString());
        if (origClientOrderId != null) params.put("origClientOrderId", origClientOrderId);
        return performSignedDelete("/v3/order", params);
    }

    public Map<String, Object> cancelOcoOrder(String symbol, Long orderListId, String listClientOrderId) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (orderListId != null) params.put("orderListId", orderListId.toString());
        if (listClientOrderId != null) params.put("listClientOrderId", listClientOrderId);
        return performSignedDelete("/v3/orderList", params);
    }

    public List<Map<String, Object>> getOpenOrders(String symbol) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        return performSignedGetList("/v3/openOrders", params);
    }

    public List<Map<String, Object>> getAllOrders(String symbol, Long startTime, Long endTime) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", symbol);
        if (startTime != null) params.put("startTime", startTime.toString());
        if (endTime != null) params.put("endTime", endTime.toString());
        return performSignedGetList("/v3/allOrders", params);
    }

    private Map<String, Object> performSignedPost(String path, Map<String, String> params) {
        String query = buildQuery(params);
        String url = path + "?" + query;
        log.info("📤 [POST] URL: {}", url);
        try {
            return binanceWebClient.post()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("❌ [POST] Error: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> performSignedDelete(String path, Map<String, String> params) {
        String query = buildQuery(params);
        String url = path + "?" + query;
        log.info("🗑️ [DELETE] URL: {}", url);
        try {
            return binanceWebClient.delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("❌ [DELETE] Error: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private List<Map<String, Object>> performSignedGetList(String path, Map<String, String> params) {
        String query = buildQuery(params);
        String url = path + "?" + query;
        log.info("📥 [GET] URL: {}", url);
        try {
            return binanceWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToFlux(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .collectList()
                    .block();
        } catch (Exception e) {
            log.error("❌ [GET] Error: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    private Map<String, String> buildOrderParams(OrderRequestDTO request) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("symbol", request.getSymbol());
        params.put("side", request.getSide());
        params.put("type", request.getType());
        if (request.getQuantity() != null) params.put("quantity", request.getQuantity().toString());
        if (request.getPrice() != null) params.put("price", request.getPrice().toString());
        if (request.getTimeInForce() != null) params.put("timeInForce", request.getTimeInForce());
        return params;
    }

    private String buildQuery(Map<String, String> params) {
        long timestamp = timeUtil.getServerTimestamp();
        params.put("timestamp", String.valueOf(timestamp));
        String query = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b).orElse("");
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());
        return query + "&signature=" + signature;
    }
}
