package tech.noetzold.strategy_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.strategy_api.dto.OrderOcoRequestDTO;
import tech.noetzold.strategy_api.dto.OrderRequestDTO;
import tech.noetzold.strategy_api.service.BinanceOrderService;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
@Slf4j
public class TradeController {

    private final BinanceOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO dto) {
        log.info("POST /api/trade/order called with symbol={}, side={}, type={}, quantity={}, price={}",
                dto.getSymbol(), dto.getSide(), dto.getType(), dto.getQuantity(), dto.getPrice());
        return ResponseEntity.ok(orderService.placeOrder(dto));
    }

    @PostMapping("/order/oco")
    public ResponseEntity<?> placeOcoOrder(@RequestBody OrderOcoRequestDTO dto) {
        log.info("POST /api/trade/order/oco called with symbol={}, quantity={}, price={}, stopPrice={}, stopLimitPrice={}",
                dto.getSymbol(), dto.getQuantity(), dto.getPrice(), dto.getStopPrice(), dto.getStopLimitPrice());
        return ResponseEntity.ok(orderService.placeOcoOrder(dto));
    }

    @DeleteMapping("/order")
    public ResponseEntity<?> cancelOrder(
            @RequestParam String symbol,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) String origClientOrderId
    ) {
        log.info("DELETE /api/trade/order called with symbol={}, orderId={}, origClientOrderId={}",
                symbol, orderId, origClientOrderId);
        return ResponseEntity.ok(orderService.cancelOrder(symbol, orderId, origClientOrderId));
    }

    @DeleteMapping("/order/oco")
    public ResponseEntity<?> cancelOcoOrder(
            @RequestParam String symbol,
            @RequestParam(required = false) Long orderListId,
            @RequestParam(required = false) String listClientOrderId
    ) {
        log.info("DELETE /api/trade/order/oco called with symbol={}, orderListId={}, listClientOrderId={}",
                symbol, orderListId, listClientOrderId);
        return ResponseEntity.ok(orderService.cancelOcoOrder(symbol, orderListId, listClientOrderId));
    }

    @GetMapping("/orders/open")
    public ResponseEntity<?> listOpenOrders(@RequestParam String symbol) {
        log.info("GET /api/trade/orders/open called with symbol={}", symbol);
        return ResponseEntity.ok(orderService.getOpenOrders(symbol));
    }

    @GetMapping("/orders/all")
    public ResponseEntity<?> listAllOrders(
            @RequestParam String symbol,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime
    ) {
        log.info("GET /api/trade/orders/all called with symbol={}, startTime={}, endTime={}",
                symbol, startTime, endTime);
        return ResponseEntity.ok(orderService.getAllOrders(symbol, startTime, endTime));
    }
}
