package tech.noetzold.config_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableConfigServer
public class ConfigApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigApiApplication.class, args);
	}

}
