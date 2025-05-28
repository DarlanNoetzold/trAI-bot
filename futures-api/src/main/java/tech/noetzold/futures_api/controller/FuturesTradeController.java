package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.dto.FuturesOrderDTO;
import tech.noetzold.futures_api.service.AuditService;
import tech.noetzold.futures_api.service.FuturesTradeService;

import java.util.Map;

@RestController
@RequestMapping("/api/futures/trade")
@RequiredArgsConstructor
@Slf4j
public class FuturesTradeController {

    private final FuturesTradeService futuresTradeService;
    private final AuditService auditService;

    private String extractEmail(Map<String, String> headers) {
        return headers.getOrDefault("x-email", "unknown");
    }

    private Long extractUserId(Map<String, String> headers) {
        try {
            return Long.parseLong(headers.getOrDefault("x-user-id", "-1"));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestBody FuturesOrderDTO orderDTO,
            @RequestHeader Map<String, String> headers) {

        log.info("POST /api/futures/trade/order called with symbol={}, side={}, type={}, quantity={}, price={}, tif={}",
                orderDTO.getSymbol(), orderDTO.getSide(), orderDTO.getType(),
                orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTimeInForce());

        auditService.log(
                extractUserId(headers),
                extractEmail(headers),
                "CREATE",
                "FUTURES_ORDER",
                "Created futures order for symbol: " + orderDTO.getSymbol()
        );

        return ResponseEntity.ok(futuresTradeService.createOrder(orderDTO));
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> cancelOrder(
            @RequestParam String symbol,
            @RequestParam Long orderId,
            @RequestHeader Map<String, String> headers) {

        log.info("DELETE /api/futures/trade/order called with symbol={}, orderId={}", symbol, orderId);

        auditService.log(
                extractUserId(headers),
                extractEmail(headers),
                "DELETE",
                "FUTURES_ORDER",
                "Canceled futures order with ID: " + orderId + " for symbol: " + symbol
        );

        return ResponseEntity.ok(futuresTradeService.cancelOrder(symbol, orderId));
    }
}
