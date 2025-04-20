package tech.noetzold.notification_api.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tech.noetzold.notification_api.dto.NotificationMessage;
import tech.noetzold.notification_api.service.TelegramService;
import tech.noetzold.notification_api.service.WhatsAppService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final TelegramService telegramService;
    private final WhatsAppService whatsAppService;

    @RabbitListener(queues = "notifications.queue")
    public void receiveNotification(NotificationMessage message) {
        log.info("📩 Notificação recebida: {}", message);
        telegramService.sendNotification(message);
        whatsAppService.sendNotification(message);
    }
}
