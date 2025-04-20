package tech.noetzold.notification_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.notification_api.dto.NotificationMessage;
import tech.noetzold.notification_api.model.User;
import tech.noetzold.notification_api.repository.UserRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final WebClient client = WebClient.create("https://api.callmebot.com");
    private final UserRepository userRepository;

    private final String API_KEY = "SEU_API_KEY"; // Preferencialmente injetado via application.yml/env

    public void sendNotification(NotificationMessage message) {
        userRepository.findByEmail(message.getUserEmail()).ifPresentOrElse(user -> {
            String phone = user.getWhatsappNumber();
            if (phone == null || phone.isBlank()) {
                log.warn("❌ WhatsApp number não encontrado para o usuário {}", message.getUserEmail());
                return;
            }

            String formattedMessage = formatMessage(message);

            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/whatsapp.php")
                            .queryParam("phone", phone)
                            .queryParam("text", formattedMessage)
                            .queryParam("apikey", API_KEY)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> log.info("✅ Mensagem enviada via WhatsApp: {}", response));
        }, () -> {
            log.warn("❌ Usuário não encontrado no banco: {}", message.getUserEmail());
        });
    }

    private String formatMessage(NotificationMessage msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("🔔 ").append(msg.getType()).append("\n");
        sb.append("🧠 Estratégia: ").append(msg.getStrategyName()).append("\n");
        sb.append("💡 Ação: ").append(msg.getAction()).append("\n");
        sb.append("📈 Símbolo: ").append(msg.getSymbol()).append("\n");
        sb.append("🕒 ").append(msg.getTimestamp()).append("\n");
        sb.append("🔧 Origem: ").append(msg.getOriginApi()).append(" | ").append(msg.getEnvironment()).append("\n");
        sb.append("📦 Parâmetros:\n");
        for (Map.Entry<String, String> entry : msg.getParameters().entrySet()) {
            sb.append("• ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
