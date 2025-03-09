package kg.nurtelecom.backend_application.mapper;

import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ProductsRowMapper implements RowMapper<ProductResponse> {

    @Override
    public ProductResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ProductResponse(
                (UUID) rs.getObject("product_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock"),
                rs.getString("image_url")
        );
    }
}
