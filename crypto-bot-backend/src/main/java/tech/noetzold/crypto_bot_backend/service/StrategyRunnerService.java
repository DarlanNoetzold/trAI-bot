package tech.noetzold.crypto_bot_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;
import tech.noetzold.crypto_bot_backend.context.BinanceEnvironmentContext;
import tech.noetzold.crypto_bot_backend.enums.BinanceEnvironment;
import tech.noetzold.crypto_bot_backend.model.User;
import tech.noetzold.crypto_bot_backend.strategy.BaseStrategy;

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
    private final UserService userService;

    public StrategyRunnerService(List<BaseStrategy> strategyBeanList, UserService userService) {
        this.userService = userService;
        this.strategyBeans = strategyBeanList.stream()
                .collect(Collectors.toMap(
                        bean -> bean.getClass().getAnnotation(Component.class).value(),
                        bean -> bean
                ));
    }

    public List<String> listAvailableStrategies() {
        return new ArrayList<>(strategyBeans.keySet());
    }

    public String runStrategy(String strategyName, Map<String, String> params) {
        BaseStrategy strategy = strategyBeans.get(strategyName);
        if (strategy == null) {
            return "❌ Estratégia não encontrada: " + strategyName;
        }

        try {
            User user = getAuthenticatedUser();
            String apiKey = getApiKey(user);
            String secretKey = getSecretKey(user);

            log.info("🚀 Executando estratégia: {} com usuário {}", strategyName, user.getEmail());
            return strategy.run(params, apiKey, secretKey);

        } catch (Exception e) {
            log.error("❌ Erro ao obter usuário autenticado ou executar estratégia", e);
            return "Erro na execução da estratégia: " + e.getMessage();
        }
    }

    public String runStrategyAsync(String strategyName, Map<String, String> params) {
        if (runningStrategies.containsKey(strategyName)) {
            return "⚠️ Estratégia já está em execução: " + strategyName;
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        long interval = getIntervalInMillis(params.getOrDefault("interval", "60"));

        Runnable task = () -> {
            try {
                SecurityContextHolder.setContext(securityContext);

                while (!Thread.currentThread().isInterrupted()) {
                    publishLog(strategyName, "⏳ Executando estratégia '" + strategyName + "'...");
                    String result = runStrategy(strategyName, params);
                    publishLog(strategyName, "✅ Resultado: " + result);
                    Thread.sleep(interval);
                }

            } catch (InterruptedException e) {
                publishLog(strategyName, "🛑 Estratégia '" + strategyName + "' interrompida.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("❌ Erro na execução da estratégia: {}", strategyName, e);
                publishLog(strategyName, "❌ Erro: " + e.getMessage());
            } finally {
                SecurityContextHolder.clearContext();
            }
        };

        Runnable securedTask = DelegatingSecurityContextRunnable.create(task, securityContext);
        Future<?> future = executor.submit(securedTask);
        runningStrategies.put(strategyName, future);
        return "🟢 Execução da estratégia iniciada: " + strategyName;
    }

    public String stopStrategy(String strategyName) {
        Future<?> future = runningStrategies.remove(strategyName);
        if (future != null) {
            future.cancel(true);
            publishLog(strategyName, "🛑 Estratégia parada: " + strategyName);
            return "🛑 Estratégia parada: " + strategyName;
        } else {
            return "⚠️ Estratégia não estava em execução: " + strategyName;
        }
    }

    public boolean isStrategyRegistered(String strategyName) {
        return strategyBeans.containsKey(strategyName);
    }

    // ========= 🔔 WebSocket Log Broadcasting ========= //

    public void registerLogSubscriber(String strategyName, FluxSink<String> sink) {
        logSubscribers.put(strategyName, sink);
        publishLog(strategyName, "📡 Conectado para receber logs da estratégia '" + strategyName + "'");
    }

    public void unregisterLogSubscriber(String strategyName) {
        logSubscribers.remove(strategyName);
    }

    private void publishLog(String strategyName, String logMessage) {
        log.info("[{}] {}", strategyName, logMessage);
        FluxSink<String> sink = logSubscribers.get(strategyName);
        if (sink != null) {
            sink.next(logMessage);
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
            return 60_000L; // fallback
        }
    }

    private User getAuthenticatedUser() {
        var context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null || context.getAuthentication().getName() == null) {
            throw new IllegalStateException("Usuário não autenticado no contexto");
        }

        String email = context.getAuthentication().getName();
        return (User) userService.loadUserByUsername(email);
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
