package kg.nurtelecom.backend_application.facades;

import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ProductFacade {
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(UUID id);
    void saveProduct(ProductSaveRequest productSaveRequest, MultipartFile imageFile);
    void updateProduct(UUID id, ProductRequestForm productRequestForm, MultipartFile imageFile);
    void deleteProduct(UUID id);
}
