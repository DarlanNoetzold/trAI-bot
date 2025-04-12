package tech.noetzold.crypto_bot_backend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import tech.noetzold.crypto_bot_backend.model.User;
import tech.noetzold.crypto_bot_backend.service.StrategyRunnerService;
import tech.noetzold.crypto_bot_backend.service.UserService;
import tech.noetzold.crypto_bot_backend.util.JwtUtil;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class StrategyLogWebSocketHandler implements WebSocketHandler {

    private final StrategyRunnerService strategyRunnerService;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final Map<String, FluxSink<String>> sinks = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String query = session.getHandshakeInfo().getUri().getQuery();
        String token = null;
        String strategyName = null;

        // 🔍 Extrair token e strategyName com segurança
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    switch (key) {
                        case "token" -> token = value;
                        case "strategyName" -> strategyName = value;
                    }
                }
            }
        }

        if (token == null || token.isBlank() || strategyName == null || strategyName.isBlank()) {
            log.warn("WebSocket encerrado: parâmetro 'token' ou 'strategyName' ausente ou inválido.");
            return session.close();
        }

        log.info("🌐 Solicitando conexão WebSocket para estratégia '{}'", strategyName);

        try {
            String email = jwtUtil.extractUsername(token);
            User user = (User) userService.loadUserByUsername(email);

            if (!jwtUtil.validateToken(token, user)) {
                log.warn("❌ Token JWT inválido para usuário '{}'. Conexão WebSocket encerrada.", email);
                return session.close();
            }

            final String strategy = strategyName; // 👈 torna efetivamente final

            Flux<String> logStream = Flux.create(sink -> {
                sinks.put(strategy, sink);
                strategyRunnerService.registerLogSubscriber(strategy, sink);
                sink.onDispose(() -> {
                    sinks.remove(strategy);
                    strategyRunnerService.unregisterLogSubscriber(strategy);
                });
            });


            log.info("✅ WebSocket estabelecido para '{}'", strategyName);
            return session.send(logStream.map(session::textMessage));

        } catch (Exception e) {
            log.error("🔥 Erro ao validar o token ou carregar usuário no WebSocket", e);
            return session.close();
        }
    }
}
