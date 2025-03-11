package kg.nurtelecom.backend_application.services;

import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    UUID createOrder(List<OrderItemsRequest> orderItemsRequests, UUID userId, BigDecimal totalAmount,
                     boolean isDelivered, String deliveryAddress);
    void deleteOrderById(UUID id);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(UUID id);
    List<OrderItemsResponse> getOrderItemsByOrderId(UUID orderId);
    void updateOrderById(UUID id, List<OrderItemsRequest> orderItemsRequests, boolean isDelivered, String deliveryAddress);
    List<OrderResponse> searchOrdersByUserId(UUID userId);
    List<OrderItemsRequest> convertToOrderItemsRequest(List<OrderItemsResponse> orderItemsResponses);

}
