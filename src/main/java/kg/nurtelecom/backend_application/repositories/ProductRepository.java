package kg.nurtelecom.backend_application.repositories;

import kg.nurtelecom.backend_application.jdbc.JdbcConnection;
import kg.nurtelecom.backend_application.mapper.ProductsRowMapper;
import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.services.ProductService;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Types;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ProductRepository implements ProductService {

    private final JdbcConnection jdbcConnection;
    private static final String SQL_QUERY_CREATE_PRODUCT = "INSERT INTO products (name, description, price, stock, image_url, product_type) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_GET_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String SQL_QUERY_UPDATE_PRODUCT_BY_ID = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?,  product_type = ?::product_type, image_url = ? WHERE product_id = ?";
    private static final String SQL_QURY_GET_PRODUCT_BY_ID = "SELECT * FROM products WHERE product_id = ?";
    private static final String SQL_QUERY_DELETE_PRODUCT_BY_ID = "DELETE FROM products WHERE product_id = ?";

    public ProductRepository(JdbcConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> products = new ArrayList<>();
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY_GET_ALL_PRODUCTS);
            ResultSet rs = ps.executeQuery();
            ProductsRowMapper rowMapper = new ProductsRowMapper();
            int rowNum = 0;
            while (rs.next()) {
                products.add(rowMapper.mapRow(rs, rowNum++));
            }
            rs.close();
            ps.close();
            connection.close();
            return products;
        } catch (SQLException e) {
            throw new RuntimeException("Error getting all products", e);
        }
    }

    @Override
    public void save(ProductSaveRequest productSaveRequest) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY_CREATE_PRODUCT);

            ps.setString(1, productSaveRequest.getName());
            ps.setString(2, productSaveRequest.getDescription());
            ps.setBigDecimal(3, productSaveRequest.getPrice());
            ps.setInt(4, productSaveRequest.getStock());
            ps.setString(5, productSaveRequest.getImageUrl());
            ps.setObject(6, productSaveRequest.getProductType().name(), Types.OTHER);
            ps.executeUpdate();

            ps.close();
            connection.close();

        } catch (SQLException sqlException) {
            throw new RuntimeException("Cannot save product to DB " + sqlException);
        }
    }

    @Override
    public void update(ProductRequestForm productRequestForm) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY_UPDATE_PRODUCT_BY_ID);
            ps.setString(1, productRequestForm.getName());
            ps.setString(2, productRequestForm.getDescription());
            ps.setBigDecimal(3, productRequestForm.getPrice());
            ps.setInt(4, productRequestForm.getStock());
            ps.setObject(5, productRequestForm.getProductType().name(), Types.OTHER);
            ps.setString(6, productRequestForm.getImageUrl());
            ps.setObject(7, productRequestForm.getId());
            ps.executeUpdate();

            ps.close();
            connection.close();

        } catch (SQLException sqlException) {
            throw new RuntimeException("Cannot update product to DB " + sqlException);
        }
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_QURY_GET_PRODUCT_BY_ID);
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();

            ProductsRowMapper rowMapper = new ProductsRowMapper();
            rs.next();
            ProductResponse product = rowMapper.mapRow(rs, 0);
            rs.close();
            ps.close();
            connection.close();

            return product;

        } catch (SQLException sqlException) {
            throw new RuntimeException("Cannot get product by id " + id, sqlException);
        }
    }

    @Override
    public void deleteProductById(UUID id) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY_DELETE_PRODUCT_BY_ID);
            ps.setObject(1, id);
            int affectedRows = ps.executeUpdate();
            System.out.println(affectedRows);

        } catch (SQLException sqlException) {
            throw new RuntimeException("Cannot delete product by id " + id, sqlException);
        }
    }
}
