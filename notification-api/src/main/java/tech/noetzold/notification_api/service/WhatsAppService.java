package tech.noetzold.notification_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tech.noetzold.notification_api.dto.NotificationMessage;
import tech.noetzold.notification_api.repository.UserRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WhatsAppService {

    private final WebClient client = WebClient.create("https://api.callmebot.com");
    private final UserRepository userRepository;

    public void sendNotification(NotificationMessage message) {
        userRepository.findByEmail(message.getUserEmail()).ifPresentOrElse(user -> {
            String phone = user.getWhatsappNumber();
            String apiKey = user.getWhatsappApiKey();

            if (phone == null || phone.isBlank()) {
                log.warn("❌ WhatsApp number não encontrado para o usuário {}", message.getUserEmail());
                return;
            }

            if (apiKey == null || apiKey.isBlank()) {
                log.warn("❌ API key do WhatsApp não encontrada para o usuário {}", message.getUserEmail());
                return;
            }

            String formattedMessage = formatMessage(message);
            String encodedMessage = URLEncoder.encode(formattedMessage, StandardCharsets.UTF_8);

            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/whatsapp.php")
                            .queryParam("phone", phone)
                            .queryParam("text", encodedMessage)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(error -> log.error("❌ Erro ao enviar mensagem via WhatsApp: {}", error.getMessage()))
                    .retry(2)
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

        Map<String, String> parameters = msg.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            sb.append("📦 Parâmetros:\n");
            parameters.forEach((key, value) ->
                    sb.append("• ").append(key).append(": ").append(value).append("\n")
            );
        }

        return sb.toString();
    }
}
