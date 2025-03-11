package kg.nurtelecom.backend_application.payload.requests;

import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;

import java.util.List;

public record OrderDetailsRequest(
    OrderResponse order,
    List<OrderItemsResponse> orderItems)
{}

