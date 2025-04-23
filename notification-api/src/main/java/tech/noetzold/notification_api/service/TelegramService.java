package tech.noetzold.notification_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tech.noetzold.notification_api.dto.NotificationMessage;
import tech.noetzold.notification_api.repository.UserRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

    private final WebClient telegramClient; // injete um WebClient configurado
    private final UserRepository userRepository;

    @Value("${telegram.bot-token}")
    private String botToken;

    public void sendNotification(NotificationMessage message) {
        userRepository.findByEmail(message.getUserEmail()).ifPresentOrElse(user -> {
            String chatId = user.getTelegramChatId();
            if (chatId == null || chatId.isBlank()) {
                log.warn("❌ Telegram chat ID não encontrado para o usuário {}", message.getUserEmail());
                return;
            }

            String formattedMessage = formatMessage(message);

            telegramClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/bot{token}/sendMessage")
                            .queryParam("chat_id", chatId)
                            .queryParam("text", formattedMessage)
                            .build(botToken))
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnError(WebClientResponseException.class, e ->
                            log.error("❌ Telegram API respondeu com erro HTTP {}: {}", e.getStatusCode(), e.getResponseBodyAsString()))
                    .doOnError(e ->
                            log.error("❌ Erro inesperado ao enviar mensagem via Telegram: {}", e.getMessage()))
                    .retry(2)
                    .subscribe(response ->
                            log.info("✅ Mensagem enviada via Telegram: {}", response));
        }, () -> log.warn("❌ Usuário não encontrado no banco: {}", message.getUserEmail()));
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
            parameters.forEach((k, v) -> sb.append("• ").append(k).append(": ").append(v).append("\n"));
        }

        return sb.toString();
    }
}
