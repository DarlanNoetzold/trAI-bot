package tech.noetzold.strategy_api.strategy;

import java.util.Map;

public interface BaseStrategy {

    /**
     * Executa a estratégia com os parâmetros informados e retorna uma saída (mensagem ou resultado).
     * @param params parâmetros como símbolo, intervalo, limiares, etc.
     * @param apiKey chave da Binance do usuário autenticado
     * @param secretKey chave secreta da Binance do usuário autenticado
     * @return mensagem com o resultado da execução
     */
    String run(Map<String, String> params, String apiKey, String secretKey);
}
