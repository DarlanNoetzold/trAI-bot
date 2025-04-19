package tech.noetzold.strategy_api.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class BinanceSignatureUtil {

    public static String generateSignature(String data, String secretKey) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            byte[] hash = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(b & 0xff);
                if (hex.length() == 1) result.append('0');
                result.append(hex);
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar assinatura HMAC-SHA256", e);
        }
    }
}
