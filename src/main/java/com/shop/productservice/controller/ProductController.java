package com.shop.productservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.productservice.entity.Product;
import com.shop.productservice.repository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader("tus-correlation-id") String correlationId) {
        logger.info("Inside getAllProducts API with CorrelationId: {}", correlationId);
        List<Product> products = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id,
                                                   @RequestHeader("tus-correlation-id") String correlationId) {
        logger.info("Inside getProductById API with CorrelationId: {} and id: {}", correlationId, id);
        Product product = productRepository.findById(id)
                .orElse(null);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product,
                                                  @RequestHeader("tus-correlation-id") String correlationId) {
        logger.info("Inside createProduct API with CorrelationId: {}", correlationId);
        Product createdProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product productDetails,
                                                  @RequestHeader("tus-correlation-id") String correlationId) {
        logger.info("Inside updateProduct API with CorrelationId: {} and id: {}", correlationId, id);
        Product product = productRepository.findById(id)
                .orElse(null);

        if (product != null) {
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            // Update other attributes if needed

            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                 @RequestHeader("tus-correlation-id") String correlationId) {
        logger.info("Inside deleteProduct API with CorrelationId: {} and id: {}", correlationId, id);
        boolean deleted = productRepository.existsById(id);
        if (deleted) {
            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Product deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found with id: " + id);
        }
    }
}