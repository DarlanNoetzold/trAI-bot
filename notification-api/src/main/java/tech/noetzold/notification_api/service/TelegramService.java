package tech.noetzold.notification_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.notification_api.dto.NotificationMessage;
import tech.noetzold.notification_api.model.User;
import tech.noetzold.notification_api.repository.UserRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

    private final WebClient client = WebClient.create("https://api.telegram.org");
    private final UserRepository userRepository;

    @Value("${telegram.bot-token}")
    private String BOT_TOKEN;

    public void sendNotification(NotificationMessage message) {
        userRepository.findByEmail(message.getUserEmail()).ifPresentOrElse(user -> {
            String chatId = user.getTelegramChatId();
            if (chatId == null || chatId.isBlank()) {
                log.warn("❌ Telegram chat ID não encontrado para o usuário {}", message.getUserEmail());
                return;
            }

            String formattedMessage = formatMessage(message);

            client.get()
                    .uri("/bot{botToken}/sendMessage?chat_id={chatId}&text={text}",
                            BOT_TOKEN, chatId, formattedMessage)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> log.info("✅ Mensagem enviada via Telegram: {}", response));
        }, () -> {
            log.warn("❌ Usuário não encontrado no banco: {}", message.getUserEmail());
        });
    }

    private String formatMessage(NotificationMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("📢 *Notificação*\n");
        sb.append("👤 ").append(msg.getUsername()).append(" (").append(msg.getUserEmail()).append(")\n");
        sb.append("🔧 ").append(msg.getOriginApi()).append(" | ").append(msg.getEnvironment()).append("\n");
        sb.append("📈 Tipo: ").append(msg.getType()).append("\n");
        sb.append("💡 Ação: ").append(msg.getAction()).append("\n");
        if (msg.getStrategyName() != null) sb.append("🧠 Estratégia: ").append(msg.getStrategyName()).append("\n");
        sb.append("🪙 Símbolo: ").append(msg.getSymbol()).append("\n");
        sb.append("🕒 Horário: ").append(msg.getTimestamp()).append("\n");
        sb.append("\n📦 Parâmetros:\n");
        for (Map.Entry<String, String> entry : msg.getParameters().entrySet()) {
            sb.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
