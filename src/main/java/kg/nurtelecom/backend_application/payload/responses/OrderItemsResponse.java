package kg.nurtelecom.backend_application.payload.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemsResponse(
        UUID orderItemId,
        UUID orderId,
        UUID productId,
        Integer quantity,
        BigDecimal price
) {}
