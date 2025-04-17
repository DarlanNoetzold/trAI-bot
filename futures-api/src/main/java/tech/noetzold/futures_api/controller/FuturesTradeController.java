package tech.noetzold.futures_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.futures_api.dto.FuturesOrderDTO;
import tech.noetzold.futures_api.service.FuturesTradeService;

@RestController
@RequestMapping("/api/futures/trade")
@RequiredArgsConstructor
public class FuturesTradeController {

    private final FuturesTradeService futuresTradeService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody FuturesOrderDTO orderDTO) {
        return ResponseEntity.ok(futuresTradeService.createOrder(orderDTO));
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> cancelOrder(@RequestParam String symbol, @RequestParam Long orderId) {
        return ResponseEntity.ok(futuresTradeService.cancelOrder(symbol, orderId));
    }
}

