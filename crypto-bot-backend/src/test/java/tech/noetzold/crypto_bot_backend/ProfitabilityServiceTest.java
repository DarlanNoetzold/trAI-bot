package tech.noetzold.crypto_bot_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.noetzold.crypto_bot_backend.service.BinanceAccountService;
import tech.noetzold.crypto_bot_backend.service.ProfitabilityService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProfitabilityServiceTest {

    private BinanceAccountService accountService;
    private ProfitabilityService profitabilityService;

    @BeforeEach
    public void setup() {
        accountService = mock(BinanceAccountService.class);
        profitabilityService = new ProfitabilityService(accountService);
    }

    @Test
    public void testCalculateRealizedProfit() {
        List<Map> mockTrades = List.of(
                Map.of("price", "100.0", "qty", "1", "isBuyer", "true"),
                Map.of("price", "110.0", "qty", "1", "isBuyer", "false")
        );

        when(accountService.getAccountTrades("BTCUSDT", 1000)).thenReturn(mockTrades);

        BigDecimal profit = profitabilityService.calculateRealizedProfit("BTCUSDT");

        assertEquals(new BigDecimal("10.0"), profit);
    }
}
