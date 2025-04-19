package tech.noetzold.spot_api.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class TimeUtil {

    private final WebClient webClient;

    public TimeUtil(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.binance.com").build();
    }

    public long getServerTimestamp() {
        try {
            return webClient.get()
                    .uri("/api/v3/time")
                    .retrieve()
                    .bodyToMono(ServerTimeResponse.class)
                    .block()
                    .getServerTime();
        } catch (Exception e) {
            log.error("Erro ao buscar timestamp do servidor Binance", e);
            return System.currentTimeMillis();
        }
    }

    private static class ServerTimeResponse {
        private long serverTime;

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }
    }
}
