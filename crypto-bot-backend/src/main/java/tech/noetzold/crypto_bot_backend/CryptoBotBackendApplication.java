package tech.noetzold.crypto_bot_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.noetzold.crypto_bot_backend.config.BinanceProperties;

@SpringBootApplication
@EnableConfigurationProperties(BinanceProperties.class)
public class CryptoBotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoBotBackendApplication.class, args);
	}

}
