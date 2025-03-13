package kg.nurtelecom.backend_application.facades;

import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import org.springframework.ui.Model;
import java.util.UUID;

public interface UserOrderFacade {
    String searchOrders(UUID userId, Model model);
    String getOrderById(UUID id, Model model);
    String deleteOrder(UUID id);
    String editOrderForm(UUID id, Model model);
    String updateOrder(UUID id, OrderDetailsRequest orderDetails);
    String getCreateOrderPage(Model model);
    String createOrder(OrderItemsRequestsDTO orderItemsDTO);
}
