package me.shamanov.oms.rest;

import me.shamanov.oms.dto.Products;
import me.shamanov.oms.model.Product;
import me.shamanov.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"/", "/products/json"}, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public Products getProductsAsJSON() {
        return this.getProducts();
    }

    @GetMapping(value = "/products/xml", produces = {MimeTypeUtils.APPLICATION_XML_VALUE})
    public Products getProductsAsXML() {
        return this.getProducts();
    }

    private Products getProducts() {
        List<Product> productList = this.productService.getAllProducts();
        return new Products(productList);
    }
}
