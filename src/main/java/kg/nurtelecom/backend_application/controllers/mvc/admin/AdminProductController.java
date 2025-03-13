package kg.nurtelecom.backend_application.controllers.mvc.admin;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.AdminProductFacade;
import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import kg.nurtelecom.backend_application.utils.ImageValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductFacade adminProductFacade;

    public AdminProductController(AdminProductFacade adminProductFacade) {
        this.adminProductFacade = adminProductFacade;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        try {
            List<ProductResponse> products = adminProductFacade.getAllProducts();
            model.addAttribute("products", products);
            return "products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading products: " + e.getMessage());
            return "error-page";
        }
    }

    @GetMapping("/add")
    public String showAddProductForm(@ModelAttribute ProductRequestForm productRequestForm) {
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductSaveRequest productSaveRequest,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") @ImageValidator(
                                     maxSize = 5 * 1024 * 1024,
                                     allowedTypes = {"image/jpeg", "image/png"}
                             ) MultipartFile imageFile,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", formatValidationErrors(bindingResult));
            return "error-page";
        }

        try {
            adminProductFacade.saveProduct(productSaveRequest, imageFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving product: " + e.getMessage());
            return "error-page";
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") UUID id, Model model) {
        try {
            ProductResponse product = adminProductFacade.getProductById(id);
            if (product == null) {
                throw new IllegalArgumentException("Product not found");
            }
            model.addAttribute("productRequestForm", product);
            return "edit-product";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error-page";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") UUID id,
                                @Valid @ModelAttribute ProductRequestForm productRequestForm,
                                BindingResult bindingResult,
                                @RequestParam("imageFile") @ImageValidator(
                                        maxSize = 5 * 1024 * 1024,
                                        allowedTypes = {"image/jpeg", "image/png"}
                                ) MultipartFile imageFile,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", formatValidationErrors(bindingResult));
            return "error-page";
        }

        try {
            adminProductFacade.updateProduct(id, productRequestForm, imageFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating product: " + e.getMessage());
            return "error-page";
        }

        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") UUID id, Model model) {
        try {
            adminProductFacade.deleteProduct(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting product: " + e.getMessage());
            return "error-page";
        }
        return "redirect:/admin/products";
    }

    private String formatValidationErrors(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("Validation errors: ");

        for (ObjectError error : bindingResult.getAllErrors()) {
            errorBuilder.append(error.getDefaultMessage());
            errorBuilder.append("; ");
        }

        if (errorBuilder.length() > 0) {
            errorBuilder.setLength(errorBuilder.length() - 2);
        }

        return errorBuilder.toString();
    }
}