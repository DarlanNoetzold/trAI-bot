package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.dto.OrderOcoRequestDTO;
import tech.noetzold.crypto_bot_backend.dto.OrderRequestDTO;
import tech.noetzold.crypto_bot_backend.service.BinanceOrderService;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final BinanceOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.placeOrder(dto));
    }

    @PostMapping("/order/oco")
    public ResponseEntity<?> placeOcoOrder(@RequestBody OrderOcoRequestDTO dto) {
        return ResponseEntity.ok(orderService.placeOcoOrder(dto));
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> cancelOrder(
            @RequestParam String symbol,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String origClientOrderId
    ) {
        return ResponseEntity.ok(orderService.cancelOrder(symbol, orderId, origClientOrderId));
    }

    @DeleteMapping("/order/oco")
    public ResponseEntity<?> cancelOcoOrder(
            @RequestParam String symbol,
            @RequestParam(required = false) Long orderListId,
            @RequestParam(required = false) String listClientOrderId
    ) {
        return ResponseEntity.ok(orderService.cancelOcoOrder(symbol, orderListId, listClientOrderId));
    }

    @GetMapping("/orders/open")
    public ResponseEntity<?> listOpenOrders(@RequestParam String symbol) {
        return ResponseEntity.ok(orderService.getOpenOrders(symbol));
    }

    @GetMapping("/orders/all")
    public ResponseEntity<?> listAllOrders(
            @RequestParam String symbol,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(symbol, startTime, endTime));
    }
}
