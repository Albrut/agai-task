package kg.nurtelecom.backend_application.facades.supports;

import kg.nurtelecom.backend_application.facades.OrderFacade;
import kg.nurtelecom.backend_application.facades.ProductFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequest;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class OrderFacadeSupport implements OrderFacade {

    private final OrderService orderService;
    private final ProductFacade productFacade;

    public OrderFacadeSupport(OrderService orderService, ProductFacade productFacade) {
        this.orderService = orderService;
        this.productFacade = productFacade;
    }

    @Override
    public String getAllOrders(Model model) {
        List<OrderResponse> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @Override
    public String getOrderById(UUID id, Model model) {
        OrderResponse order = orderService.getOrderById(id);
        List<OrderItemsResponse> orderItems = orderService.getOrderItemsByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "order-details";
    }

    @Override
    public String deleteOrder(UUID id) {
        orderService.deleteOrderById(id);
        return "redirect:/admin/orders";
    }

    @Override
    public String searchOrders(UUID userId, Model model) {
        List<OrderResponse> orders = orderService.searchOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @Override
    public String editOrderForm(UUID id, Model model) {
        OrderResponse order = orderService.getOrderById(id);
        List<OrderItemsResponse> orderItems = orderService.getOrderItemsByOrderId(id);
        OrderDetailsRequest orderDetails = new OrderDetailsRequest(order, orderItems);
        model.addAttribute("orderDetails", orderDetails);
        List<ProductResponse> products = productFacade.getAllProducts();
        model.addAttribute("availableProducts", products);
        return "order-edit";
    }

    @Override
    public String updateOrder(UUID id, OrderDetailsRequest orderDetails) {
        List<OrderItemsRequest> requests = new ArrayList<>();

        List<OrderItemsResponse> orderItems = orderDetails.orderItems() != null
                ? orderDetails.orderItems() : Collections.emptyList();

        for (OrderItemsResponse item : orderItems) {
            if (item == null) {
                continue;
            }
            OrderItemsRequest request = new OrderItemsRequest(
                    item.orderItemId(),
                    item.productId(),
                    item.quantity(),
                    item.price()
            );
            requests.add(request);
        }

        orderService.updateOrderById(
                id,
                requests,
                orderDetails.order().isDelivered(),
                orderDetails.order().deliveryAddress()
        );
        return "redirect:/admin/orders";
    }

    @Override
    public String getCreateOrderPage(Model model) {
        List<ProductResponse> products = productFacade.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("orderItemsDTO", new OrderItemsRequestsDTO());
        return "order-create";
    }

    @Override
    public String createOrder(OrderItemsRequestsDTO orderItemsDTO) {
        List<OrderItemsRequest> orderItemsRequests = orderItemsDTO.getOrderItemsRequests();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemsRequest item : orderItemsRequests) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        UUID userUuid = UUID.fromString("2b7c846f-f8c7-4673-a183-8b6cb00d658e");
        UUID newOrderId = orderService.createOrder(
                orderItemsRequests,
                userUuid,
                totalAmount,
                orderItemsDTO.isDelivered(),
                orderItemsDTO.getDeliveryAddress()
        );
        return "redirect:/admin/orders/edit/" + newOrderId;
    }



    @Override
    public ResponseEntity<List<OrderResponse>> getAllOrdersRest() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrderByIdRest(UUID id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @Override
    public ResponseEntity<Void> deleteOrderRest(UUID id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<OrderResponse>> searchOrdersRest(UUID userId) {
        List<OrderResponse> orders = orderService.searchOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @Override
    public ResponseEntity<OrderResponse> updateOrderRest(UUID id, OrderDetailsRequest orderDetails) {
        List<OrderItemsRequest> requests = new ArrayList<>();
        List<OrderItemsResponse> orderItems = orderDetails.orderItems() != null
                ? orderDetails.orderItems() : Collections.emptyList();

        for (OrderItemsResponse item : orderItems) {
            if (item != null) {
                OrderItemsRequest request = new OrderItemsRequest(
                        item.orderItemId(),
                        item.productId(),
                        item.quantity(),
                        item.price()
                );
                requests.add(request);
            }
        }

        orderService.updateOrderById(
                id,
                requests,
                orderDetails.order().isDelivered(),
                orderDetails.order().deliveryAddress()
        );
        OrderResponse updatedOrder = orderService.getOrderById(id);
        return ResponseEntity.ok(updatedOrder);
    }

    @Override
    public ResponseEntity<OrderResponse> createOrderRest(OrderItemsRequestsDTO orderItemsDTO) {
        List<OrderItemsRequest> orderItemsRequests = orderItemsDTO.getOrderItemsRequests();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemsRequest item : orderItemsRequests) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        UUID userUuid = UUID.fromString("2b7c846f-f8c7-4673-a183-8b6cb00d658e");
        UUID newOrderId = orderService.createOrder(
                orderItemsRequests,
                userUuid,
                totalAmount,
                orderItemsDTO.isDelivered(),
                orderItemsDTO.getDeliveryAddress()
        );
        OrderResponse createdOrder = orderService.getOrderById(newOrderId);
        return ResponseEntity.status(201).body(createdOrder);
    }
}

