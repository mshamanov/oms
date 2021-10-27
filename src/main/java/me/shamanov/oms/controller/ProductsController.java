package me.shamanov.oms.controller;

import me.shamanov.oms.model.Order;
import me.shamanov.oms.model.Product;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping
@SessionAttributes("currentOrder")
public class ProductsController {
    private final ProductService productService;


    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProductsPage(@RequestParam(value = "search", required = false) String search,
                                  @RequestParam(defaultValue = "name") String sort,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "asc") String direction,
                                  Model model) {
        Sort.Order sortOrder = new Sort.Order(Sort.Direction.fromString(direction), sort).ignoreCase();
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sortOrder));

        Page<Product> productPage;
        if (search != null && !search.isBlank()) {
            productPage = this.productService.getProductsBySearchValue(search, pageRequest);
        } else {
            productPage = this.productService.getAllProducts(pageRequest);
        }

        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("products", productPage.getContent());
        return "products";
    }

    @GetMapping("/addProduct")
    public String getAddProductPage(Model model) {
        model.addAttribute("pageTitle", "Adding new product");
        model.addAttribute("product", new Product());
        return "product-edit";
    }

    @GetMapping("/updateProduct")
    public String getUpdateProductPage(@RequestParam Long id, Model model) {
        Product existingProduct =
                this.productService.getProductById(id).orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Product not found"));
        model.addAttribute("pageTitle", "Updating product");
        model.addAttribute("product", existingProduct);
        return "product-edit";
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id, Model model) {
        if (!this.productService.isProductExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Object order = model.getAttribute("currentOrder");
        String clearSessionProductId = "";

        if (order != null) {
            Order currentOrder = (Order) order;
            if (currentOrder.getCustomer() != null) {
                clearSessionProductId = "?clearProductId=" + id;
            }
        }

        this.productService.deleteProductById(id);
        return "redirect:/products" + clearSessionProductId;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute Product product, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("pageTitle", (product.getId() != null ? "Updating" : "Adding") + " new product");
            return "product-edit";
        } else {
            this.productService.saveProduct(product);
            return "redirect:/products";
        }
    }
}
