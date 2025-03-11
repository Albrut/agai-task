package kg.nurtelecom.backend_application.mapper;
import kg.nurtelecom.backend_application.payload.responses.OrderResponse;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OrderRowMapper implements RowMapper<OrderResponse> {
    @Override
    public OrderResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderResponse(
                rs.getObject("order_id", UUID.class),
                rs.getBigDecimal("total_amount"),
                rs.getObject("user_id", UUID.class),
                rs.getBoolean("is_delivered"),
                rs.getString("delivery_address")
        );
    }
}
