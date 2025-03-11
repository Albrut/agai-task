package kg.nurtelecom.backend_application.facades.supports;

import kg.nurtelecom.backend_application.facades.ProductFacade;
import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.services.ProductService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
class ProductFacadeSupport implements ProductFacade {

    private final ProductService productService;
    private static final String UPLOAD_DIR = "uploads";

    public ProductFacadeSupport(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    public ProductResponse getProductById(UUID id) {
        return productService.getProductById(id);
    }

    public void saveProduct(ProductSaveRequest productSaveRequest, MultipartFile imageFile) {
        String imageUrl = saveImage(imageFile);
        productSaveRequest.setImageUrl(imageUrl);
        productService.save(productSaveRequest);
    }

    public void updateProduct(UUID id, ProductRequestForm productRequestForm, MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String imageUrl = saveImage(imageFile);
            productRequestForm.setImageUrl(imageUrl);
            deleteImage(productService.getProductById(id).imageUrl(),id);
        } else {
            ProductResponse existingProduct = productService.getProductById(id);
            productRequestForm.setImageUrl(existingProduct.imageUrl());
        }
        productRequestForm.setId(id);
        productService.update(productRequestForm);
    }

    public void deleteProduct(UUID id) {
        deleteImage(productService.getProductById(id).imageUrl(),id);
        productService.deleteProductById(id);
    }

    private String saveImage(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new RuntimeException("Image file is empty");
        }
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileExtension = ".jpeg";
            String originalFilename = imageFile.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed save product's image" + e);
        }
    }

    private void deleteImage(String imageUrl, UUID id) {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        Path imagePath = uploadPath.resolve(Path.of(imageUrl));
        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting image " + e);
        }

    }
}
