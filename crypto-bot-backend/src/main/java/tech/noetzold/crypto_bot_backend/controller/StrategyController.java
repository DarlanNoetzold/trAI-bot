package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.service.StrategyRunnerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyRunnerService strategyRunnerService;

    @GetMapping("/list")
    public ResponseEntity<List<String>> listStrategies() {
        return ResponseEntity.ok(strategyRunnerService.listAvailableStrategies());
    }

    @PostMapping("/run/{strategyName}")
    public ResponseEntity<String> runStrategy(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        // executa de forma contínua (loop com sleep)
        String result = strategyRunnerService.runStrategyAsync(strategyName, params);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/run-once/{strategyName}")
    public ResponseEntity<String> runStrategyOnce(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        // executa uma única vez
        String result = strategyRunnerService.runStrategy(strategyName, params);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/stop/{strategyName}")
    public ResponseEntity<String> stopStrategy(@PathVariable String strategyName) {
        String result = strategyRunnerService.stopStrategy(strategyName);
        return ResponseEntity.ok(result);
    }

}
