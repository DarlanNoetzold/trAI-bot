package tech.noetzold.spot_api.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tech.noetzold.spot_api.dto.NotificationMessage;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "notifications.exchange";
    private static final String ROUTING_KEY = "notifications.key";

    public void sendNotification(
            String type,
            String userEmail,
            String userId,
            String username,
            String strategyName,
            String symbol,
            String environment,
            String originApi,
            String action,
            Map<String, String> parameters
    ) {

        NotificationMessage message = NotificationMessage.builder()
                .type(type)
                .userEmail(userEmail)
                .userId(userId)
                .username(username)
                .strategyName(strategyName)
                .symbol(symbol)
                .environment(environment)
                .originApi(originApi)
                .action(action)
                .parameters(parameters)
                .timestamp(Instant.now())
                .build();

        log.info("🔔 Enviando notificação para fila RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }
}

