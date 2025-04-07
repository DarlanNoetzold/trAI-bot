package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.service.StrategyRunnerService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/strategies")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyRunnerService strategyRunnerService;
    private final Map<String, Process> runningStrategies = new ConcurrentHashMap<>();

    @GetMapping("/list")
    public ResponseEntity<List<String>> listStrategies() {
        return ResponseEntity.ok(strategyRunnerService.listAvailableStrategies());
    }

    @PostMapping("/run/{strategyName}")
    public ResponseEntity<String> runStrategy(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        String output = strategyRunnerService.runStrategy(strategyName, params);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/stop/{strategyName}")
    public ResponseEntity<String> stopStrategy(@PathVariable String strategyName) {
        Process process = runningStrategies.get(strategyName);
        if (process != null && process.isAlive()) {
            process.destroy();
            runningStrategies.remove(strategyName);
            return ResponseEntity.ok("Estratégia parada: " + strategyName);
        }
        return ResponseEntity.badRequest().body("Estratégia não está em execução");
    }

    @PostMapping("/run-once/{strategyName}")
    public ResponseEntity<String> runStrategyOnce(
            @PathVariable String strategyName,
            @RequestBody Map<String, String> params
    ) {
        String output = strategyRunnerService.runStrategy(strategyName, params);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/custom")
    public ResponseEntity<String> runCustomStrategy(
            @RequestBody String pythonCode
    ) {
        String output = strategyRunnerService.runCustomScript(pythonCode, Map.of());
        return ResponseEntity.ok(output);
    }
}
