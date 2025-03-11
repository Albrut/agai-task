package kg.nurtelecom.backend_application.payload.responses;

import kg.nurtelecom.backend_application.payload.requests.enums.ProductTypeEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        ProductTypeEnum productType,
        String imageUrl

) {
}
