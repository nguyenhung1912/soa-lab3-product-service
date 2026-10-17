package com.soa.product;

import com.soa.product.dto.AuthResponse;
import com.soa.product.dto.ProductRequest;
import com.soa.product.entity.Product;
import com.soa.product.service.AuthClientService;
import com.soa.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Product Management service and JWT authentication filter.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SoaLab3ProductServiceApplicationTests {

	@Autowired
	private ProductService productService;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthClientService authClientService;

	/**
	 * Verifies CRUD operations at the service layer.
	 */
	@Test
	void contextLoadsAndCrudWorks() {
		List<Product> initialProducts = productService.getAllProducts();
		assertTrue(initialProducts.size() >= 3);

		ProductRequest createReq = new ProductRequest(
				"Tai nghe Sony WH-1000XM5",
				"Tai nghe chống ồn chủ động cao cấp",
				new BigDecimal("6990000.00"),
				15
		);
		Product created = productService.createProduct(createReq);
		assertNotNull(created.getId());
		assertEquals("Tai nghe Sony WH-1000XM5", created.getName());

		ProductRequest updateReq = new ProductRequest(
				"Tai nghe Sony WH-1000XM5 (New)",
				"Phiên bản mới cập nhật",
				new BigDecimal("6490000.00"),
				20
		);
		Optional<Product> updated = productService.updateProduct(created.getId(), updateReq);
		assertTrue(updated.isPresent());
		assertEquals(new BigDecimal("6490000.00"), updated.get().getPrice());
		assertEquals(20, updated.get().getQuantity());

		boolean deleted = productService.deleteProduct(created.getId());
		assertTrue(deleted);
		assertTrue(productService.getProductById(created.getId()).isEmpty());
	}

	/**
	 * Verifies 401 Unauthorized when no token is provided and 200/201 when validated via Lab 2 Auth Service.
	 */
	@Test
	void testRestEndpointsWithAndWithoutAuth() throws Exception {
		// 1. Request without Authorization header -> 401 Unauthorized
		mockMvc.perform(get("/products"))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.success").value(false));

		// Mock valid response from Lab 2 Auth Service
		Mockito.when(authClientService.validateTokenWithAuthService("Bearer valid-jwt-token"))
				.thenReturn(new AuthResponse(true, "Token hợp lệ", "valid-jwt-token", "admin"));

		// 2. GET /products -> 200 OK
		mockMvc.perform(get("/products")
						.header("Authorization", "Bearer valid-jwt-token"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").exists());

		// 3. POST /products -> 201 Created
		String newProductJson = """
				{
				  "name": "iPad Pro M4 11 inch",
				  "description": "Man hinh OLED Ultra Retina XDR",
				  "price": 26990000.00,
				  "quantity": 12
				}
				""";
		mockMvc.perform(post("/products")
						.header("Authorization", "Bearer valid-jwt-token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(newProductJson))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("iPad Pro M4 11 inch"));

		// 4. GET /products/1 -> 200 OK
		mockMvc.perform(get("/products/1")
						.header("Authorization", "Bearer valid-jwt-token"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));

		// 5. PUT /products/1 -> 200 OK
		String updateProductJson = """
				{
				  "name": "Bàn phím cơ Keychron K8 Pro (Updated)",
				  "description": "Updated description",
				  "price": 1990000.00,
				  "quantity": 30
				}
				""";
		mockMvc.perform(put("/products/1")
						.header("Authorization", "Bearer valid-jwt-token")
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateProductJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bàn phím cơ Keychron K8 Pro (Updated)"))
				.andExpect(jsonPath("$.quantity").value(30));

		// 6. DELETE /products/2 -> 200 OK
		mockMvc.perform(delete("/products/2")
						.header("Authorization", "Bearer valid-jwt-token"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

}
