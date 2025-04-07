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

    // Retorna a lista de estratégias disponíveis na pasta /strategies
    @GetMapping
    public ResponseEntity<List<String>> listStrategies() {
        return ResponseEntity.ok(strategyRunnerService.listAvailableStrategies());
    }

    // Executa uma estratégia Python existente (com parâmetros se necessário)
    @PostMapping("/run/{strategyName}")
    public ResponseEntity<String> runStrategy(
            @PathVariable String strategyName,
            @RequestBody(required = false) Map<String, String> params
    ) {
        String output = strategyRunnerService.runStrategy(strategyName, params);
        return ResponseEntity.ok(output);
    }

    // Executa um script Python customizado, enviado diretamente no corpo da requisição
    @PostMapping("/custom")
    public ResponseEntity<String> runCustomStrategy(
            @RequestBody Map<String, Object> request
    ) {
        String pythonCode = (String) request.get("code");
        Map<String, String> params = (Map<String, String>) request.get("params");
        String output = strategyRunnerService.runCustomScript(pythonCode, params);
        return ResponseEntity.ok(output);
    }
}
