package kg.nurtelecom.backend_application.controllers.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.ProductFacade;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequest;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequestsDTO;
import kg.nurtelecom.backend_application.payload.requests.OrderDetailsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
    private final OrderService orderService;
    private final ProductFacade productFacade;

    public OrderController(OrderService orderService, ProductFacade productFacade) {
        this.productFacade = productFacade;
        this.orderService = orderService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<OrderResponse> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable UUID id, Model model) {
        OrderResponse order = orderService.getOrderById(id);
        List<OrderItemsResponse> orderItems = orderService.getOrderItemsByOrderId(id);
        model.addAttribute("order", order);
        model.addAttribute("orderItems", orderItems);
        return "order-details";
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrderById(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/search")
    public String searchOrders(@RequestParam UUID userId, Model model) {
        List<OrderResponse> orders = orderService.searchOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable UUID id, Model model) {
        OrderResponse order = orderService.getOrderById(id);
        List<OrderItemsResponse> orderItems = orderService.getOrderItemsByOrderId(id);
        OrderDetailsRequest orderDetails = new OrderDetailsRequest(order, orderItems);
        model.addAttribute("orderDetails", orderDetails);
        return "order-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@Valid
            @PathVariable UUID id,
                              @ModelAttribute("orderDetails") OrderDetailsRequest orderDetails
    ) {
        List<OrderItemsRequest> requests = new ArrayList<>();
        List<OrderItemsResponse> orderItems = orderDetails.orderItems();

        for (OrderItemsResponse item : orderItems) {
            OrderItemsRequest request = new OrderItemsRequest();
            request.setId(item.orderItemId());
            request.setOrderId(id);
            request.setProductId(item.productId());
            request.setQuantity(item.quantity());
            request.setPrice(item.price());
            requests.add(request);
        }

        orderService.updateOrderById(id, requests,orderDetails.order().isDelivered(), orderDetails.order().deliveryAddress());
        return "redirect:/admin/orders";
    }


    @GetMapping("/create")
    public String getCreateOrderPage(Model model) {
        List<ProductResponse> products = productFacade.getAllProducts();
        model.addAttribute("products", products);
        return "order-create";
    }


    @PostMapping("/create")
    public String createOrder(@ModelAttribute OrderItemsRequestsDTO orderItemsDTO) {
        List<OrderItemsRequest> orderItemsRequests = orderItemsDTO.getOrderItemsRequests();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemsRequest item : orderItemsRequests) {
            totalAmount = totalAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        UUID uuid = UUID.fromString("2b7c846f-f8c7-4673-a183-8b6cb00d658e");
        UUID newOrderId = orderService.createOrder(orderItemsRequests, uuid, totalAmount,
                orderItemsDTO.isDelivered(),
                orderItemsDTO.getDeliveryAddress());
        return "redirect:/admin/orders/edit/" + newOrderId;
    }
}


