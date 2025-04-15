package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.noetzold.crypto_bot_backend.dto.CustomStrategyDTO;
import tech.noetzold.crypto_bot_backend.model.CustomStrategy;
import tech.noetzold.crypto_bot_backend.repository.CustomStrategyRepository;

import java.util.List;

@RestController
@RequestMapping("/api/custom-strategies")
@RequiredArgsConstructor
public class CustomStrategyController {

    private final CustomStrategyRepository repository;

    @PostMapping
    public ResponseEntity<?> createStrategy(@RequestBody CustomStrategyDTO dto) {
        CustomStrategy strategy = convertDtoToEntity(dto);
        repository.save(strategy);
        return ResponseEntity.ok("Criada com sucesso");
    }


    @GetMapping
    public List<CustomStrategy> listStrategies() {
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
