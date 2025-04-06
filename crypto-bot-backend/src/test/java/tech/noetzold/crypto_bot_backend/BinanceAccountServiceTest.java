package tech.noetzold.crypto_bot_backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;
import tech.noetzold.crypto_bot_backend.service.BinanceAccountService;
import tech.noetzold.crypto_bot_backend.util.TimeUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class BinanceAccountServiceTest {

    private BinanceAccountService accountService;

    @BeforeEach
    public void setup() {
        WebClient webClient = WebClient.create("https://testnet.binance.vision/api");
        BinanceProperties props = new BinanceProperties();
        props.setSecretKey("test");
        props.setApiKey("test");

        TimeUtil timeUtil = mock(TimeUtil.class);
        Mockito.when(timeUtil.getServerTimestamp()).thenReturn(System.currentTimeMillis());

        accountService = new BinanceAccountService(webClient, props, timeUtil);
    }

    @Test
    public void testGetAccountInfo() {
        Map<String, Object> info = accountService.getAccountInfo();
        assertNotNull(info); // irá falhar se não estiver conectado à Binance testnet
    }
}
