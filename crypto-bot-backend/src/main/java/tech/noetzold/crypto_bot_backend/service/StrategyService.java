package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.noetzold.crypto_bot_backend.model.Strategy;
import tech.noetzold.crypto_bot_backend.repository.StrategyRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;

    public Strategy saveStrategy(Strategy strategy) {
        return strategyRepository.save(strategy);
    }

    public List<Strategy> listAllStrategies() {
        return strategyRepository.findAll();
    }

    public Optional<Strategy> getStrategyById(Long id) {
        return strategyRepository.findById(id);
    }

    public void deleteStrategy(Long id) {
        strategyRepository.deleteById(id);
    }

    public Strategy activateStrategy(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estratégia não encontrada"));
        strategy.setActive(true);
        return strategyRepository.save(strategy);
    }

    public Strategy deactivateStrategy(Long id) {
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estratégia não encontrada"));
        strategy.setActive(false);
        return strategyRepository.save(strategy);
    }
}
