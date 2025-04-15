package tech.noetzold.crypto_bot_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    public ResponseEntity<CustomStrategy> createStrategy(@RequestBody CustomStrategyDTO dto) {
        CustomStrategy strategy = new CustomStrategy();
        BeanUtils.copyProperties(dto, strategy);
        return ResponseEntity.ok(repository.save(strategy));
    }

    @GetMapping
    public List<CustomStrategy> listStrategies() {
        return repository.findAll();
    }
}
