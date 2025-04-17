package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.service.StrategyRunnerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
@Slf4j
public class StrategyController {

    private final StrategyRunnerService strategyRunnerService;

    @GetMapping("/list")
    public ResponseEntity<List<String>> listStrategies() {
        log.info("GET /api/strategies/list called");
        return ResponseEntity.ok(strategyRunnerService.listAvailableStrategies());
    }

    @PostMapping("/run/{strategyName}")
    public ResponseEntity<String> runStrategy(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        log.info("POST /api/strategies/run/{} called with params={}", strategyName, params);
        String result = strategyRunnerService.runStrategyAsync(strategyName, params);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/run-once/{strategyName}")
    public ResponseEntity<String> runStrategyOnce(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        log.info("POST /api/strategies/run-once/{} called with params={}", strategyName, params);
        String result = strategyRunnerService.runStrategy(strategyName, params);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/stop/{strategyName}")
    public ResponseEntity<String> stopStrategy(@PathVariable String strategyName) {
        log.info("POST /api/strategies/stop/{} called", strategyName);
        String result = strategyRunnerService.stopStrategy(strategyName);
        return ResponseEntity.ok(result);
    }
}
