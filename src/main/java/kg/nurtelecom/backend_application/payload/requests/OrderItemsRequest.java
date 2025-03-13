package kg.nurtelecom.backend_application.payload.requests;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemsRequest {

    private UUID id;
    private UUID productId;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemsRequest(
            UUID id,
            UUID productId,
            Integer quantity,
            BigDecimal price
    ) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemsRequest() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
