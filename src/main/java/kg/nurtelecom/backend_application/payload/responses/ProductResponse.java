package kg.nurtelecom.backend_application.payload.responses;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl

) {
}
