package kg.nurtelecom.backend_application.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtil {
    private static final String SECRET = "MySuperSecretKey";
    private static final String HMAC_ALGO = "HmacSHA256";

    public static String generateToken(String username) {
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + 3600000;
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + username + "\",\"iat\":" + (nowMillis/1000) + ",\"exp\":" + (expMillis/1000) + "}";
        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
        String signature = sign(encodedHeader + "." + encodedPayload);
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    private static String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(keySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
        } catch(Exception e) {
            throw new RuntimeException("Error signing token", e);
        }
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if(parts.length != 3) return false;

            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            if(!sign(header + "." + payload).equals(signature)) return false;

            String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            return isTokenNotExpired(payloadJson);
        } catch(Exception e) {
            return false;
        }
    }

    private static boolean isTokenNotExpired(String payloadJson) {
        String[] fields = payloadJson.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");

        for(String field : fields) {
            String[] parts = field.split(":", 2);
            if(parts.length == 2 && "exp".equals(parts[0].trim())) {
                try {
                    long expTime = Long.parseLong(parts[1].trim());
                    return (System.currentTimeMillis() / 1000) <= expTime;
                } catch(NumberFormatException e) {
                    return false;
                }
            }
        }
        return false;
    }

    public static String getUsernameFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if(parts.length != 3) return null;

            String payload = parts[1];
            String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            return extractSubject(payloadJson);
        } catch(Exception e) {
            return null;
        }
    }

    private static String extractSubject(String payloadJson) {
        String[] fields = payloadJson.replace("{", "")
                .replace("}", "")
                .replace("\"", "")
                .split(",");

        for(String field : fields) {
            String[] parts = field.split(":", 2);
            if(parts.length == 2 && "sub".equals(parts[0].trim())) {
                return parts[1].trim();
            }
        }
        return null;
    }
}