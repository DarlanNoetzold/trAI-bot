package tech.noetzold.futures_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.futures_api.util.BinanceSignatureUtil;
import tech.noetzold.futures_api.util.TimeUtil;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FuturesUserDataService {

    @Qualifier("binanceWebClient")
    private final WebClient binanceWebClient;

    public Map<String, Object> createListenKey() {
        return postWithoutParams("/fapi/v1/listenKey");
    }

    public Map<String, Object> keepAliveListenKey(String listenKey) {
        return putWithQuery("/fapi/v1/listenKey", "listenKey=" + listenKey);
    }

    public Map<String, Object> deleteListenKey(String listenKey) {
        return deleteWithQuery("/fapi/v1/listenKey", "listenKey=" + listenKey);
    }

    private Map<String, Object> postWithoutParams(String path) {
        try {
            return binanceWebClient.post()
                    .uri(path)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(msg -> new RuntimeException("Erro: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro POST {}: {}", path, e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> putWithQuery(String path, String query) {
        try {
            return binanceWebClient.put()
                    .uri(path + "?" + query)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(msg -> new RuntimeException("Erro: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro PUT {}: {}", path, e.getMessage(), e);
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> deleteWithQuery(String path, String query) {
        try {
            return binanceWebClient.delete()
                    .uri(path + "?" + query)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(String.class).map(msg -> new RuntimeException("Erro: " + msg)))
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Erro DELETE {}: {}", path, e.getMessage(), e);
            return Collections.emptyMap();
        }
    }
}