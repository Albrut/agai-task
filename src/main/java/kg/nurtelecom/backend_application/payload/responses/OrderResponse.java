package kg.nurtelecom.backend_application.payload.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        @JsonProperty("order_id") UUID id,
        @JsonProperty("total_amount") BigDecimal totalAmount,
        @JsonProperty("user_id") UUID userId,
        @JsonProperty("is_delivered") Boolean isDelivered,
        @JsonProperty("delivery_address") String deliveryAddress,
        @JsonProperty("username_of_owner") String usernameOfOwner
) {
}
