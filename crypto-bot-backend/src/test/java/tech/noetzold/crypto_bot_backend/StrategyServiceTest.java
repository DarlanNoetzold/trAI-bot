package tech.noetzold.crypto_bot_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.noetzold.crypto_bot_backend.model.Strategy;
import tech.noetzold.crypto_bot_backend.repository.StrategyRepository;
import tech.noetzold.crypto_bot_backend.service.StrategyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StrategyServiceTest {

    private StrategyService strategyService;
    private StrategyRepository strategyRepository;

    @BeforeEach
    public void setUp() {
        strategyRepository = mock(StrategyRepository.class);
        strategyService = new StrategyService(strategyRepository);
    }

    @Test
    public void testActivateStrategy() {
        Strategy strategy = new Strategy();
        strategy.setId(1L);
        strategy.setActive(false);

        when(strategyRepository.findById(1L)).thenReturn(Optional.of(strategy));
        when(strategyRepository.save(strategy)).thenReturn(strategy);

        Strategy activated = strategyService.activateStrategy(1L);

        assertTrue(activated.isActive());
        verify(strategyRepository).save(strategy);
    }

    @Test
    public void testDeactivateStrategy() {
        Strategy strategy = new Strategy();
        strategy.setId(2L);
        strategy.setActive(true);

        when(strategyRepository.findById(2L)).thenReturn(Optional.of(strategy));
        when(strategyRepository.save(strategy)).thenReturn(strategy);

        Strategy deactivated = strategyService.deactivateStrategy(2L);

        assertFalse(deactivated.isActive());
        verify(strategyRepository).save(strategy);
    }
}
