package tech.noetzold.crypto_bot_backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "binance")
public class BinanceProperties {

    private String apiUrl;
    private String streamUrl;
    private String apiKey;
    private String secretKey;
    private String symbol;
    private double profitability;
}
