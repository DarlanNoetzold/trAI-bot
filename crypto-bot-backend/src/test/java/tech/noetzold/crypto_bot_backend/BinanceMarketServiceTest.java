package tech.noetzold.crypto_bot_backend;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.crypto_bot_backend.service.BinanceMarketService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BinanceMarketServiceTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private BinanceMarketService marketService;

    @Test
    public void testGetCandles() {
        // Mock será implementado em testes completos com WebClient customizado
        // Aqui verificamos apenas estrutura de chamada
        List<List<Object>> result = marketService.getCandles("BTCUSDT", "5m", 10);
        assertNotNull(result); // Requer mock real para funcionar
    }
}
