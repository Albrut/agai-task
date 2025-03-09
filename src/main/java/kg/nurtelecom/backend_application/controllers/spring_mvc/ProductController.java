package kg.nurtelecom.backend_application.controllers.spring_mvc;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductResponse> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(@ModelAttribute ProductRequestForm productRequestForm) {
        System.out.println(productRequestForm.getDescription());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductSaveRequest productSaveRequest,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            return "error-page";
        }

        if (!imageFile.isEmpty()) {
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileExtension  = ".jpeg";
                String originalFilename = imageFile.getOriginalFilename();
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String fileName = UUID.randomUUID().toString() + fileExtension;
                Path filePath = uploadPath.resolve(fileName);

                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                productSaveRequest.setImageUrl(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "error-page";
            }
        }

        productService.save(productSaveRequest);
        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") UUID id, Model model) {
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("productRequestForm", product);
        return "edit-product";
    }


    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") UUID id,
                                @Valid @ModelAttribute ProductRequestForm productRequestForm,
                                BindingResult bindingResult,
                                @RequestParam("imageFile") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            return "error-page";
        }
        if (!imageFile.isEmpty()) {
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);

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
                productRequestForm.setImageUrl(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "error-page";
            }
        }
        else {
            ProductResponse product= productService.getProductById(id);
            productRequestForm.setImageUrl(product.imageUrl());
        }
        productRequestForm.setId(id);
        productService.update(productRequestForm);
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
