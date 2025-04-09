package tech.noetzold.crypto_bot_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;
import tech.noetzold.crypto_bot_backend.dto.OrderRequestDTO;
import tech.noetzold.crypto_bot_backend.service.BinanceOrderService;
import tech.noetzold.crypto_bot_backend.util.TimeUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class BinanceOrderServiceTest {

    private BinanceOrderService orderService;

    @BeforeEach
    public void setUp() {
        WebClient webClient = WebClient.create("https://testnet.binance.vision/api");
        BinanceProperties properties = new BinanceProperties();
        properties.setSecretKey("test");
        properties.setApiKey("test");
        TimeUtil timeUtil = mock(TimeUtil.class);
        Mockito.when(timeUtil.getServerTimestamp()).thenReturn(System.currentTimeMillis());

        orderService = new BinanceOrderService(webClient, properties, timeUtil);
    }

    @Test
    public void testCreateMarketOrderRequestStructure() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setSymbol("BTCUSDT");
        request.setSide("BUY");
        request.setType("MARKET");
        request.setQuantity(0.001);

        Map<String, Object> result = orderService.placeOrder(request);
        assertNotNull(result); // só passa com testnet habilitada
    }
}
