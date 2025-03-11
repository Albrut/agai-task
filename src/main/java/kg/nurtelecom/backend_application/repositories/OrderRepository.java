package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.mapper.OrderItemsRowMapper;
import kg.nurtelecom.backend_application.mapper.OrderRowMapper;
import kg.nurtelecom.backend_application.payload.requests.OrderItemsRequest;
import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import kg.nurtelecom.backend_application.services.OrderService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepository implements OrderService {

    private final static String CREATE_ORDER_QUERY = "INSERT INTO orders (total_amount, user_id, is_delivered, delivery_address) VALUES (?, ?, ?, ?) RETURNING order_id";
    private final static String DELETE_ORDER_BY_ID_QUERY = "DELETE FROM orders WHERE order_id = ?";

    private final static String GET_ALL_ORDERS_QUERY =
            "SELECT o.order_id, o.total_amount, o.user_id, o.is_delivered, o.delivery_address, u.username AS usernameOfOwner " +
                    "FROM orders o " +
                    "LEFT JOIN users u ON o.user_id = u.user_id";

    private final static String GET_ORDER_BY_ID_QUERY =
            "SELECT o.order_id, o.total_amount, o.user_id, o.is_delivered, o.delivery_address, u.username AS usernameOfOwner " +
                    "FROM orders o " +
                    "LEFT JOIN users u ON o.user_id = u.user_id " +
                    "WHERE o.order_id = ?";

    private final static String GET_ORDER_ITEMS_BY_ORDER_ID_QUERY = "SELECT * FROM order_items WHERE order_id = ?";
    private final static String SEARCH_ORDERS_BY_USER_ID_QUERY = "SELECT * FROM orders WHERE user_id = ?";
    private final static String DELETE_ORDER_ITEMS_BY_ORDER_ID_QUERY = "DELETE FROM order_items WHERE order_id = ?";
    private final static String INSERT_ORDER_ITEM_QUERY = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_ORDER_TOTAL_QUERY = "UPDATE orders SET total_amount = ?, is_delivered = ?, delivery_address = ? WHERE order_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UUID createOrder(List<OrderItemsRequest> orderItemsRequests, UUID userId, BigDecimal totalAmount, boolean isDelivered, String deliveryAddress) {
        UUID newOrderId = jdbcTemplate.queryForObject(CREATE_ORDER_QUERY, UUID.class,
                totalAmount, userId, isDelivered, deliveryAddress);

        for (OrderItemsRequest item : orderItemsRequests) {
            jdbcTemplate.update(INSERT_ORDER_ITEM_QUERY, newOrderId, item.getProductId(), item.getQuantity(), item.getPrice());
        }
        return newOrderId;
    }

    @Override
    public void deleteOrderById(UUID id) {
        jdbcTemplate.update(DELETE_ORDER_BY_ID_QUERY, id);
    }

    @Override
    public List<OrderResponse> getAllOrders(){
        return jdbcTemplate.query(GET_ALL_ORDERS_QUERY, new OrderRowMapper());
    }

    @Override
    public OrderResponse getOrderById(UUID id) {
        return jdbcTemplate.queryForObject(GET_ORDER_BY_ID_QUERY, new OrderRowMapper(), id);
    }

    @Override
    public List<OrderItemsResponse> getOrderItemsByOrderId(UUID orderId) {
        return jdbcTemplate.query(GET_ORDER_ITEMS_BY_ORDER_ID_QUERY, new OrderItemsRowMapper(), orderId);
    }

    @Override
    public List<OrderResponse> searchOrdersByUserId(UUID userId) {
        return jdbcTemplate.query(SEARCH_ORDERS_BY_USER_ID_QUERY, new OrderRowMapper(), userId);
    }
    @Transactional
    @Override
    public void updateOrderById(UUID id, List<OrderItemsRequest> orderItemsRequests, boolean isDelivered, String deliveryAddress) {
        jdbcTemplate.update(DELETE_ORDER_ITEMS_BY_ORDER_ID_QUERY, id);
        BigDecimal newTotal = BigDecimal.ZERO;
        for (OrderItemsRequest item : orderItemsRequests) {
            jdbcTemplate.update(INSERT_ORDER_ITEM_QUERY, id, item.getProductId(), item.getQuantity(), item.getPrice());
            newTotal = newTotal.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        jdbcTemplate.update(UPDATE_ORDER_TOTAL_QUERY, newTotal, isDelivered, deliveryAddress, id);
    }

    @Override
    public List<OrderItemsRequest> convertToOrderItemsRequest(List<OrderItemsResponse> orderItemsResponses) {
        List<OrderItemsRequest> orderItemsRequests = new ArrayList<>();
        for (OrderItemsResponse orderItemResponse : orderItemsResponses) {
            OrderItemsRequest orderItemRequest = new OrderItemsRequest();
            orderItemRequest.setProductId(orderItemResponse.productId());
            orderItemRequest.setQuantity(orderItemResponse.quantity());
            orderItemRequest.setPrice(orderItemResponse.price());
            orderItemsRequests.add(orderItemRequest);
        }
        return orderItemsRequests;
    }

}