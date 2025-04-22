package tech.noetzold.strategy_api.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tech.noetzold.strategy_api.dto.NotificationMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "notifications.exchange";
    private static final String ROUTING_KEY = "notifications.key";

    public void send(NotificationMessage message) {
        log.info("🔔 Enviando notificação para fila RabbitMQ: {}", message);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, message);
    }
}

