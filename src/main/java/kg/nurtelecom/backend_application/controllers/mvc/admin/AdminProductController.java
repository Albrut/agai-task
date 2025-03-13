package kg.nurtelecom.backend_application.controllers.mvc.admin;

import jakarta.validation.Valid;
import kg.nurtelecom.backend_application.facades.AdminProductFacade;
import kg.nurtelecom.backend_application.payload.requests.ProductRequestForm;
import kg.nurtelecom.backend_application.payload.requests.ProductSaveRequest;
import kg.nurtelecom.backend_application.payload.responses.ProductResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductFacade adminProductFacade;

    public AdminProductController(AdminProductFacade adminProductFacade) {
        this.adminProductFacade = adminProductFacade;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductResponse> products = adminProductFacade.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(@ModelAttribute ProductRequestForm productRequestForm) {
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductSaveRequest productSaveRequest,
                             BindingResult bindingResult,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (bindingResult.hasErrors()) {
            return "error-page";
        }

        adminProductFacade.saveProduct(productSaveRequest, imageFile);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") UUID id, Model model) {
        ProductResponse product = adminProductFacade.getProductById(id);
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

        adminProductFacade.updateProduct(id, productRequestForm, imageFile);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") UUID id) {
        adminProductFacade.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
