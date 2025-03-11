package kg.nurtelecom.backend_application.mapper;

import kg.nurtelecom.backend_application.payload.responses.OrderItemsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OrderItemsRowMapper implements RowMapper<OrderItemsResponse> {
    @Override
    public OrderItemsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderItemsResponse(
                rs.getObject("order_item_id", UUID.class),
                rs.getObject("order_id", UUID.class),
                rs.getObject("product_id", UUID.class),
                rs.getInt("quantity"),
                rs.getBigDecimal("price")
        );
    }
}
