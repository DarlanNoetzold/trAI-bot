package tech.noetzold.strategy_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;
import tech.noetzold.strategy_api.context.BinanceEnvironmentContext;
import tech.noetzold.strategy_api.enums.BinanceEnvironment;
import tech.noetzold.strategy_api.model.CustomStrategy;
import tech.noetzold.strategy_api.model.StrategyExecutionLog;
import tech.noetzold.strategy_api.model.User;
import tech.noetzold.strategy_api.repository.CustomStrategyRepository;
import tech.noetzold.strategy_api.repository.StrategyExecutionLogRepository;
import tech.noetzold.strategy_api.strategy.BaseStrategy;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StrategyRunnerService {

    private final Map<String, BaseStrategy> strategyBeans;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<String, Future<?>> runningStrategies = new ConcurrentHashMap<>();
    private final Map<String, FluxSink<String>> logSubscribers = new ConcurrentHashMap<>();
    private final StrategyExecutionLogRepository strategyExecutionLogRepository;
    private final CustomStrategyRepository customStrategyRepository;

    public StrategyRunnerService(List<BaseStrategy> strategyBeanList,
                                 StrategyExecutionLogRepository strategyExecutionLogRepository,
                                 CustomStrategyRepository customStrategyRepository) {
        this.strategyExecutionLogRepository = strategyExecutionLogRepository;
        this.customStrategyRepository = customStrategyRepository;

        // 🛠️ Substituindo tentativa de usar valor de anotação @Service por nome da classe em lowercase
        this.strategyBeans = strategyBeanList.stream()
                .collect(Collectors.toMap(
                        bean -> bean.getClass().getSimpleName().toLowerCase(),  // nome da classe como chave
                        bean -> bean
                ));
    }

    public List<String> listAvailableStrategies() {
        List<String> custom = customStrategyRepository.findAll()
                .stream().map(CustomStrategy::getName).toList();
        List<String> fixed = new ArrayList<>(strategyBeans.keySet());
        fixed.addAll(custom);
        return fixed;
    }

    public String runStrategy(String strategyName, Map<String, String> params, User user) {
        if (strategyBeans.containsKey(strategyName)) {
            BaseStrategy strategy = strategyBeans.get(strategyName);
            try {
                String apiKey = getApiKey(user);
                String secretKey = getSecretKey(user);
                log.info("🚀 Executando estratégia fixa: {} com usuário {}", strategyName, user.getEmail());
                return strategy.run(params, apiKey, secretKey);
            } catch (Exception e) {
                log.error("❌ Erro ao executar estratégia fixa", e);
                return "Erro: " + e.getMessage();
            }
        } else {
            Optional<CustomStrategy> customOpt = customStrategyRepository.findByName(strategyName);
            if (customOpt.isPresent()) {
                try {
                    String apiKey = getApiKey(user);
                    String secretKey = getSecretKey(user);
                    CustomStrategy custom = customOpt.get();
                    log.info("🚀 Executando estratégia customizada: {} para {}", strategyName, user.getEmail());
                    return custom.execute(params, apiKey, secretKey);
                } catch (Exception e) {
                    log.error("❌ Erro ao executar estratégia customizada", e);
                    return "Erro na custom strategy: " + e.getMessage();
                }
            } else {
                return "❌ Estratégia não encontrada: " + strategyName;
            }
        }
    }

    public String runStrategyAsync(String strategyName, Map<String, String> params, User user) {
        if (runningStrategies.containsKey(strategyName)) {
            return "⚠️ Estratégia já está em execução: " + strategyName;
        }

        long interval = getIntervalInMillis(params.getOrDefault("interval", "60"));

        Runnable task = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    publishLog(strategyName, "⏳ Executando estratégia '" + strategyName + "'...", user);
                    String result = runStrategy(strategyName, params, user);
                    publishLog(strategyName, "✅ Resultado: " + result, user);
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                publishLog(strategyName, "🛑 Estratégia '" + strategyName + "' interrompida.", user);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("❌ Erro na execução da estratégia: {}", strategyName, e);
                publishLog(strategyName, "❌ Erro: " + e.getMessage(), user);
            }
        };

        Future<?> future = executor.submit(task);
        runningStrategies.put(strategyName, future);
        return "🟢 Execução da estratégia iniciada: " + strategyName;
    }

    public String stopStrategy(String strategyName) {
        Future<?> future = runningStrategies.remove(strategyName);
        if (future != null) {
            future.cancel(true);
            publishLog(strategyName, "🛑 Estratégia parada: " + strategyName, null);
            return "🛑 Estratégia parada: " + strategyName;
        } else {
            return "⚠️ Estratégia não estava em execução: " + strategyName;
        }
    }

    public boolean isStrategyRegistered(String strategyName) {
        return strategyBeans.containsKey(strategyName)
                || customStrategyRepository.findByName(strategyName).isPresent();
    }

    public void registerLogSubscriber(String strategyName, FluxSink<String> sink) {
        logSubscribers.put(strategyName, sink);
        publishLog(strategyName, "📡 Conectado para receber logs da estratégia '" + strategyName + "'", null);
    }

    public void unregisterLogSubscriber(String strategyName) {
        logSubscribers.remove(strategyName);
    }

    private void publishLog(String strategyName, String logMessage, User user) {
        log.info("[{}] {}", strategyName, logMessage);
        FluxSink<String> sink = logSubscribers.get(strategyName);
        if (sink != null) {
            sink.next(logMessage);
        }

        if (user != null) {
            try {
                StrategyExecutionLog logEntry = new StrategyExecutionLog();
                logEntry.setStrategyName(strategyName);
                logEntry.setMessage(logMessage);
                logEntry.setUser(user);
                strategyExecutionLogRepository.save(logEntry);
            } catch (Exception e) {
                log.warn("⚠️ Não foi possível salvar log no banco: {}", e.getMessage());
            }
        }
    }

    private long getIntervalInMillis(String intervalParam) {
        try {
            if (intervalParam.endsWith("s")) {
                return Long.parseLong(intervalParam.replace("s", "")) * 1000L;
            } else if (intervalParam.endsWith("m")) {
                return Long.parseLong(intervalParam.replace("m", "")) * 60_000L;
            } else if (intervalParam.endsWith("h")) {
                return Long.parseLong(intervalParam.replace("h", "")) * 60 * 60_000L;
            } else {
                return Long.parseLong(intervalParam) * 1000L;
            }
        } catch (NumberFormatException e) {
            return 60_000L;
        }
    }

    private String getApiKey(User user) {
        return BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                ? user.getProductionApiKey()
                : user.getTestnetApiKey();
    }

    private String getSecretKey(User user) {
        return BinanceEnvironmentContext.get() == BinanceEnvironment.PRODUCTION
                ? user.getProductionSecretKey()
                : user.getTestnetSecretKey();
    }
}
