package tech.noetzold.crypto_bot_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.noetzold.crypto_bot_backend.model.Strategy;
import tech.noetzold.crypto_bot_backend.repository.StrategyRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;

    public Strategy saveStrategy(Strategy strategy) {
        log.info("💾 [saveStrategy] Saving strategy: {}", strategy.getName());
        return strategyRepository.save(strategy);
    }

    public List<Strategy> listAllStrategies() {
        log.info("📋 [listAllStrategies] Retrieving all strategies");
        return strategyRepository.findAll();
    }

    public Optional<Strategy> getStrategyById(Long id) {
        log.info("🔍 [getStrategyById] Looking up strategy with ID: {}", id);
        return strategyRepository.findById(id);
    }

    public void deleteStrategy(Long id) {
        log.info("🗑️ [deleteStrategy] Deleting strategy with ID: {}", id);
        strategyRepository.deleteById(id);
    }

    public Strategy activateStrategy(Long id) {
        log.info("✅ [activateStrategy] Activating strategy with ID: {}", id);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("⚠️ [activateStrategy] Strategy not found for ID: {}", id);
                    return new RuntimeException("Strategy not found");
                });
        strategy.setActive(true);
        return strategyRepository.save(strategy);
    }

    public Strategy deactivateStrategy(Long id) {
        log.info("⛔ [deactivateStrategy] Deactivating strategy with ID: {}", id);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("⚠️ [deactivateStrategy] Strategy not found for ID: {}", id);
                    return new RuntimeException("Strategy not found");
                });
        strategy.setActive(false);
        return strategyRepository.save(strategy);
    }
}
