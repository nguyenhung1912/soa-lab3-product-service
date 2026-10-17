package com.soa.product.service;

import com.soa.product.dto.ProductRequest;
import com.soa.product.entity.Product;
import com.soa.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service handling business logic for product management.
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retrieves all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Retrieves a product by its ID.
     */
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    /**
     * Creates and saves a new product.
     */
    public Product createProduct(ProductRequest request) {
        validateRequest(request);

        Product product = new Product(
                request.getName().trim(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity());
        return productRepository.save(product);
    }

    /**
     * Updates an existing product by ID.
     */
    public Optional<Product> updateProduct(Integer id, ProductRequest request) {
        validateRequest(request);

        Optional<Product> existingOpt = productRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Product product = existingOpt.get();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setUpdatedAt(LocalDateTime.now());

        return Optional.of(productRepository.save(product));
    }

    /**
     * Deletes a product by ID.
     */
    public boolean deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }

    /**
     * Validates product request payload.
     */
    private void validateRequest(ProductRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu sản phẩm không được để trống");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        if (request.getPrice() == null || request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá sản phẩm không hợp lệ (phải >= 0)");
        }
        if (request.getQuantity() == null || request.getQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng sản phẩm không hợp lệ (phải >= 0)");
        }
    }
}
