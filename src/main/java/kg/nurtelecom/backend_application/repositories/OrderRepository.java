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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.UUID;

@Repository
public class OrderRepository implements OrderService {

    private final static String CREATE_ORDER_QUERY = "INSERT INTO orders (total_amount, user_id, is_delivered, delivery_address) VALUES (?, ?, ?, ?) RETURNING order_id";

    private final static String DELETE_ORDER_BY_ID_QUERY = "DELETE FROM orders WHERE order_id = ?";

    private final static String UPDATE_ORDER_ITEM_QUERY =
            "UPDATE order_items SET quantity = ?, price = ? WHERE order_id = ? AND product_id = ?";

    private final static String DELETE_ORDER_ITEM_QUERY =
            "DELETE FROM order_items WHERE order_id = ? AND product_id = ?";

    private final static String GET_ALL_ORDERS_QUERY =
            "SELECT orders.order_id, orders.total_amount, orders.user_id, orders.is_delivered, orders.delivery_address, " +
                    "users.username AS username_of_owner " +
                    "FROM orders " +
                    "JOIN users ON orders.user_id = users.user_id ";


    private final static String GET_ORDER_BY_ID_QUERY =
            "SELECT orders.order_id, orders.total_amount, orders.user_id, orders.is_delivered, orders.delivery_address, users.username AS username_of_owner " +
                    "FROM orders " +
                    "LEFT JOIN users ON orders.user_id = users.user_id " +
                    "WHERE orders.order_id = ?";

    private final static String GET_ORDER_ITEMS_BY_ORDER_ID_QUERY =
            "SELECT order_items.order_item_id, order_items.order_id, order_items.product_id, " +
                    "order_items.quantity, order_items.price, products.name AS product_name " +
                    "FROM order_items " +
                    "JOIN products ON order_items.product_id = products.product_id " +
                    "WHERE order_items.order_id = ?";



    private final static String SEARCH_ORDERS_BY_USER_ID_QUERY =
            "SELECT orders.order_id, orders.total_amount, orders.delivery_address, " +
                    "orders.is_delivered, orders.user_id, users.username AS username_of_owner " +
                    "FROM orders " +
                    "JOIN users ON orders.user_id = users.user_id " +
                    "WHERE orders.user_id = ?";

    private final static String INSERT_ORDER_ITEM_QUERY = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_ORDER_TOTAL_QUERY = "UPDATE orders SET total_amount = ?, is_delivered = ?, delivery_address = ? WHERE order_id = ?";
    private final static String UPDATE_PRODUCT_STOCK_BY_ID_QUERY =     "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public UUID createOrder(List<OrderItemsRequest> orderItemsRequests, UUID userId, BigDecimal totalAmount, boolean isDelivered, String deliveryAddress) {
        UUID newOrderId = jdbcTemplate.queryForObject(CREATE_ORDER_QUERY, UUID.class,
                totalAmount, userId, isDelivered, deliveryAddress);

        for (OrderItemsRequest item : orderItemsRequests) {
            jdbcTemplate.update(INSERT_ORDER_ITEM_QUERY, newOrderId, item.getProductId(), item.getQuantity(), item.getPrice());
            jdbcTemplate.update(UPDATE_PRODUCT_STOCK_BY_ID_QUERY, item.getQuantity(), item.getProductId(), item.getQuantity());
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
        List<OrderItemsResponse> existingItems = jdbcTemplate.query(
                GET_ORDER_ITEMS_BY_ORDER_ID_QUERY,
                new OrderItemsRowMapper(),
                id
        );

        Map<UUID, OrderItemsResponse> existingItemsMap = new HashMap<>();
        for (OrderItemsResponse item : existingItems) {
            existingItemsMap.put(item.productId(), item);
        }

        BigDecimal newTotal = BigDecimal.ZERO;

        for (OrderItemsRequest newItem : orderItemsRequests) {
            UUID productId = newItem.getProductId();
            newTotal = newTotal.add(newItem.getPrice().multiply(BigDecimal.valueOf(newItem.getQuantity())));

            if (existingItemsMap.containsKey(productId)) {
                jdbcTemplate.update(UPDATE_ORDER_ITEM_QUERY, newItem.getQuantity(), newItem.getPrice(), id, productId);
                existingItemsMap.remove(productId);
            } else {
                jdbcTemplate.update(INSERT_ORDER_ITEM_QUERY, id, productId, newItem.getQuantity(), newItem.getPrice());
            }
        }
        for (UUID productId : existingItemsMap.keySet()) {
            jdbcTemplate.update(DELETE_ORDER_ITEM_QUERY, id, productId);
        }
        jdbcTemplate.update(UPDATE_ORDER_TOTAL_QUERY, newTotal, isDelivered, deliveryAddress, id);
    }



}