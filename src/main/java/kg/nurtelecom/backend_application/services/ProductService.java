package kg.nurtelecom.backend_application.services;

import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;

import java.util.List;
import java.util.UUID;


public interface ProductService {
    List<ProductResponse> getAllProducts();
    void save(ProductSaveRequest productSaveRequest);
    void update(ProductRequestForm productRequestForm);
    ProductResponse getProductById(UUID id);
    void deleteProductById(UUID id);
}
