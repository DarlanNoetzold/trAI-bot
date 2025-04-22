package tech.noetzold.strategy_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.noetzold.strategy_api.dto.NotificationMessage;
import tech.noetzold.strategy_api.model.Strategy;
import tech.noetzold.strategy_api.producer.NotificationProducer;
import tech.noetzold.strategy_api.repository.StrategyRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyService {

    private final StrategyRepository strategyRepository;
    private final NotificationProducer notificationProducer;

    public Strategy saveStrategy(Strategy strategy) {
        log.info("💾 [saveStrategy] Saving strategy: {}", strategy.getName());
        Strategy saved = strategyRepository.save(strategy);

        sendNotification("STRATEGY_CREATED", "Saved Strategy", saved);
        return saved;
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
        strategyRepository.findById(id).ifPresent(strategy -> {
            strategyRepository.deleteById(id);
            sendNotification("STRATEGY_DELETED", "Deleted Strategy", strategy);
        });
    }

    public Strategy activateStrategy(Long id) {
        log.info("✅ [activateStrategy] Activating strategy with ID: {}", id);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("⚠️ [activateStrategy] Strategy not found for ID: {}", id);
                    return new RuntimeException("Strategy not found");
                });
        strategy.setActive(true);
        Strategy saved = strategyRepository.save(strategy);

        sendNotification("STRATEGY_ACTIVATED", "Activated Strategy", saved);
        return saved;
    }

    public Strategy deactivateStrategy(Long id) {
        log.info("⛔ [deactivateStrategy] Deactivating strategy with ID: {}", id);
        Strategy strategy = strategyRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("⚠️ [deactivateStrategy] Strategy not found for ID: {}", id);
                    return new RuntimeException("Strategy not found");
                });
        strategy.setActive(false);
        Strategy saved = strategyRepository.save(strategy);

        sendNotification("STRATEGY_DEACTIVATED", "Deactivated Strategy", saved);
        return saved;
    }

    private void sendNotification(String type, String action, Strategy strategy) {
        notificationProducer.send(NotificationMessage.builder()
                .type(type)
                .action(action)
                .strategyName(strategy.getName())
                .originApi("strategy-api")
                .timestamp(Instant.now())
                .parameters(Map.of(
                        "id", String.valueOf(strategy.getId()),
                        "description", strategy.getDescription() != null ? strategy.getDescription() : "",
                        "active", String.valueOf(strategy.isActive())
                ))
                .build());
    }
}
