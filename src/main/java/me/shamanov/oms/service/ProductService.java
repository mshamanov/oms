package me.shamanov.oms.service;

import me.shamanov.oms.model.Product;
import me.shamanov.oms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public List<Product> getAllProducts(Sort sort) {
        return this.productRepository.findAll(sort);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public List<Product> getProductsByIds(Iterable<Long> ids) {
        return this.productRepository.findAllById(ids);
    }

    public Optional<Product> getProductById(Long id) {
        return this.productRepository.findById(id);
    }

    public Page<Product> getProductsBySearchValue(String value, Pageable pageable) {
        Product product = new Product();
        product.setName(value);

        ExampleMatcher.GenericPropertyMatcher propertyMatcher =
                ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase();
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("name", propertyMatcher);
        Example<Product> productExample = Example.of(product, exampleMatcher);
        return this.productRepository.findAll(productExample, pageable);
    }

    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        this.productRepository.delete(product);
    }

    public void deleteProductById(Long id) {
        this.productRepository.deleteById(id);
    }

    public boolean isProductExists(Long id) {
        return this.productRepository.existsById(id);
    }

    public long getTotalCount() {
        return this.productRepository.count();
    }
}
