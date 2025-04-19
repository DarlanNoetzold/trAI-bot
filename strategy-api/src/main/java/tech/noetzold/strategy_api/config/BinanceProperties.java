package tech.noetzold.strategy_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import tech.noetzold.strategy_api.context.BinanceEnvironmentContext;
import tech.noetzold.strategy_api.enums.BinanceEnvironment;

@Data
@Configuration
@ConfigurationProperties(prefix = "binance")
public class BinanceProperties {

    private String testnetApiUrl;
    private String productionApiUrl;
    private String streamUrl;

    private String testnetApiKey;
    private String testnetSecretKey;
    private String productionApiKey;
    private String productionSecretKey;

    private String symbol;
    private double profitability;

    public String getDynamicApiKey() {
        return BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                ? productionApiKey
                : testnetApiKey;
    }

    public String getDynamicSecretKey() {
        return BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                ? productionSecretKey
                : testnetSecretKey;
    }
}
