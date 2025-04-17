package tech.noetzold.futures_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.futures_api.config.BinanceProperties;
import tech.noetzold.futures_api.util.BinanceSignatureUtil;
import tech.noetzold.futures_api.util.TimeUtil;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesAccountService {

    @Qualifier("binanceWebClient")
    private final WebClient webClient;
    private final BinanceProperties binanceProperties;
    private final TimeUtil timeUtil;

    public Object getBalance(String asset) {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());

        String url = "/fapi/v2/balance?" + query + "&signature=" + signature;

        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao obter saldo de futuros", e);
            return Collections.emptyList();
        }
    }

    public Object getOpenPositions() {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());

        String url = "/fapi/v2/positionRisk?" + query + "&signature=" + signature;

        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao obter posições abertas", e);
            return Collections.emptyList();
        }
    }

    public Object getFuturesAccountInfo() {
        long timestamp = timeUtil.getServerTimestamp();
        String query = "timestamp=" + timestamp;
        String signature = BinanceSignatureUtil.generateSignature(query, binanceProperties.getDynamicSecretKey());

        String url = "/fapi/v2/account?" + query + "&signature=" + signature;

        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(RuntimeException::new))
                    .bodyToMono(new ParameterizedTypeReference<>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro ao obter informações da conta de futuros", e);
            return Collections.emptyMap();
        }
    }
}
