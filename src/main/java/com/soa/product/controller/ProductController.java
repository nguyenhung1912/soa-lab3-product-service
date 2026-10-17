package com.soa.product.controller;

import com.soa.product.dto.ProductRequest;
import com.soa.product.entity.Product;
import com.soa.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller exposing CRUD endpoints for product management.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Product Management API", description = "Các RESTful API quản lý sản phẩm (Yêu cầu xác thực JWT qua Service Bài 2)")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET /products - Retrieves all products.
     */
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả các sản phẩm", description = "Trả về toàn bộ danh sách sản phẩm trong kho")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /products/{id} - Retrieves a product by ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết một sản phẩm", description = "Truy vấn thông tin chi tiết của sản phẩm theo ID")
    public ResponseEntity<?> getProductById(@PathVariable("id") Integer id) {
        Optional<Product> productOpt = productService.getProductById(id);
        if (productOpt.isPresent()) {
            return ResponseEntity.ok(productOpt.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createMessage(false, "Không tìm thấy sản phẩm với ID = " + id));
    }

    /**
     * POST /products - Creates a new product.
     */
    @PostMapping
    @Operation(summary = "Thêm một sản phẩm mới", description = "Tạo mới một sản phẩm trong database")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        try {
            Product created = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createMessage(false, e.getMessage()));
        }
    }

    /**
     * PUT /products/{id} - Updates an existing product by ID.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin sản phẩm", description = "Cập nhật tên, mô tả, giá, số lượng của sản phẩm theo ID")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Integer id,
            @RequestBody ProductRequest request) {
        try {
            Optional<Product> updatedOpt = productService.updateProduct(id, request);
            if (updatedOpt.isPresent()) {
                return ResponseEntity.ok(updatedOpt.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createMessage(false, "Không tìm thấy sản phẩm với ID = " + id + " để cập nhật"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createMessage(false, e.getMessage()));
        }
    }

    /**
     * DELETE /products/{id} - Deletes a product by ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa một sản phẩm", description = "Xóa sản phẩm khỏi database theo ID")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable("id") Integer id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.ok(createMessage(true, "Đã xóa thành công sản phẩm có ID = " + id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createMessage(false, "Không tìm thấy sản phẩm với ID = " + id + " để xóa"));
    }

    private Map<String, Object> createMessage(boolean success, String message) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("success", success);
        res.put("message", message);
        return res;
    }
}
