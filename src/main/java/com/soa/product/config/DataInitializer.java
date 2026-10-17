package com.soa.product.config;

import com.soa.product.entity.Product;
import com.soa.product.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeds initial sample product data on application startup.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                Product p1 = new Product(
                        "Bàn phím cơ Keychron K8 Pro",
                        "Bàn phím cơ không dây Bluetooth 5.1, Hotswap, Red Switch",
                        new BigDecimal("2150000.00"),
                        25
                );
                Product p2 = new Product(
                        "Chuột không dây Logitech MX Master 3S",
                        "Chuột công thái học yên tĩnh, cảm biến 8000 DPI, sạc USB-C",
                        new BigDecimal("2490000.00"),
                        40
                );
                Product p3 = new Product(
                        "Màn hình Dell UltraSharp 27 inch 4K",
                        "Màn hình đồ họa tấm nền IPS Black, độ phủ màu 100% sRGB, USB-C Hub 90W",
                        new BigDecimal("13500000.00"),
                        10
                );

                productRepository.saveAll(List.of(p1, p2, p3));
                System.out.println("Initialized 3 sample products in productdb.");
            }
        };
    }
}
