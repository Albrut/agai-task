package kg.nurtelecom.backend_application.payload.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class OrderItemsRequestsDTO {

    @NotEmpty
    private String usernameOfOwner;

    @NotEmpty
    private String deliveryAddress;

    private boolean isDelivered = false;

    @NotEmpty
    private List<OrderItemsRequest> orderItemsRequests = new ArrayList<>();


    public List<OrderItemsRequest> getOrderItemsRequests() {
        return orderItemsRequests;
    }

    public void setOrderItemsRequests(List<OrderItemsRequest> orderItemsRequests) {
        this.orderItemsRequests = orderItemsRequests;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getUsernameOfOwner() {
        return usernameOfOwner;
    }

    public void setUsernameOfOwner(String usernameOfOwner) {
        this.usernameOfOwner = usernameOfOwner;
    }

    @Override
    public String toString() {
        return "OrderItemsRequestsDTO{" +
                "usernameOfOwner='" + usernameOfOwner + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", isDelivered=" + isDelivered +
                ", orderItemsRequests=" + orderItemsRequests +
                '}';
    }


}
