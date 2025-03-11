package kg.nurtelecom.backend_application.payload.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        BigDecimal totalAmount,
        UUID userId,
        Boolean isDelivered,
        String deliveryAddress,
        String usernameOfOwner
) {
}
