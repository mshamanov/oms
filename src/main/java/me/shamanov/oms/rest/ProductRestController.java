package me.shamanov.oms.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import me.shamanov.oms.dto.Products;
import me.shamanov.oms.model.Product;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = {"Products"})
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Products as JSON/XML", description = "Get all products in JSON/XML format")
    @GetMapping(value = "/products", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Products getProducts() {
        List<Product> productList = this.productService.getAllProducts();
        return new Products(productList);
    }
}
