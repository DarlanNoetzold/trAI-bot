package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.dto.CustomStrategyDTO;
import tech.noetzold.crypto_bot_backend.model.CustomStrategy;
import tech.noetzold.crypto_bot_backend.repository.CustomStrategyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/custom-strategies")
@RequiredArgsConstructor
@Slf4j
public class CustomStrategyController {

    private final CustomStrategyRepository repository;

    @PostMapping
    public ResponseEntity<?> createStrategy(@RequestBody CustomStrategyDTO dto) {
        log.info("POST /api/custom-strategies called with userId={}, name={}, symbol={}, interval={}",
                dto.getUserId(), dto.getName(), dto.getSymbol(), dto.getInterval());

        CustomStrategy strategy = convertDtoToEntity(dto);
        repository.save(strategy);

        log.info("Custom strategy created: {}", strategy.getName());
        return ResponseEntity.ok("Criada com sucesso");
    }

    @GetMapping
    public List<CustomStrategy> listStrategies() {
        log.info("GET /api/custom-strategies called");
        return repository.findAll();
    }

    public CustomStrategy convertDtoToEntity(CustomStrategyDTO dto) {
        CustomStrategy strategy = new CustomStrategy();
        strategy.setUserId(dto.getUserId());
        strategy.setName(dto.getName());
        strategy.setSymbol(dto.getSymbol());
        strategy.setInterval(dto.getInterval());
        strategy.setPosition(dto.getPosition());
        strategy.setIndicators(dto.getIndicators());
        strategy.setStrategyCode(dto.getStrategyCode());
        return strategy;
    }
}
