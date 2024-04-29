package com.shop.productservice.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shop.productservice.entity.Product;
import com.shop.productservice.entity.ProductResp;
import com.shop.productservice.exception.ClientException;
import com.shop.productservice.exception.NoRecordFoundException;
import com.shop.productservice.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private final String BASE_URL = "http://localhost:8081/api/v2/cartinventory/";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        Optional<Product> productDb = productRepository.findById(product.getId());

        if (productDb.isPresent()) {
            throw new ClientException("Product with ID: " + product.getId() + " already exists.");
        }

        try {
            return productRepository.save(product);
        } catch (Exception ex) {
            throw new NoRecordFoundException("Product not created");
        }
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("Product not found with ID: " + id));

        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        // Update other attributes if needed

        return productRepository.save(product);
    }

    public ProductResp getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("Product not found with ID: " + id));

        // Assuming account mapping logic remains the same
        return mapProductToProductResp(product);
    }

    // Method to map Product to ProductResp
    private ProductResp mapProductToProductResp(Product product) {
        // Your mapping logic here
        return null; // Placeholder
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoRecordFoundException("Product not found with ID: " + id));
        productRepository.delete(product);
    }

    // Additional methods for handling business logic and external service calls
}
