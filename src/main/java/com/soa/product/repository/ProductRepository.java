package com.soa.product.repository;

import com.soa.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for Product entities.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
