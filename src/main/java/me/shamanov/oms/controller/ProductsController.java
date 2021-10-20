package me.shamanov.oms.controller;

import me.shamanov.oms.model.Product;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
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

        Page<Product> productPage;
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.fromString(direction), sort);

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
}
