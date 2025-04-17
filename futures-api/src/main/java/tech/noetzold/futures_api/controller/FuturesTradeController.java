package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.dto.FuturesOrderDTO;
import tech.noetzold.futures_api.service.FuturesTradeService;

@RestController
@RequestMapping("/api/futures/trade")
@RequiredArgsConstructor
@Slf4j
public class FuturesTradeController {

    private final FuturesTradeService futuresTradeService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody FuturesOrderDTO orderDTO) {
        log.info("POST /api/futures/trade/order called with symbol={}, side={}, type={}, quantity={}, price={}, tif={}",
                orderDTO.getSymbol(), orderDTO.getSide(), orderDTO.getType(),
                orderDTO.getQuantity(), orderDTO.getPrice(), orderDTO.getTimeInForce());
        return ResponseEntity.ok(futuresTradeService.createOrder(orderDTO));
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> cancelOrder(@RequestParam String symbol, @RequestParam Long orderId) {
        log.info("DELETE /api/futures/trade/order called with symbol={}, orderId={}", symbol, orderId);
        return ResponseEntity.ok(futuresTradeService.cancelOrder(symbol, orderId));
    }
}
