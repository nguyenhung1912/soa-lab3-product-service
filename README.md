# BÀI THỰC HÀNH SỐ 3: DỊCH VỤ QUẢN LÝ SẢN PHẨM (PRODUCT MANAGEMENT - SOA)

## Công nghệ sử dụng

- **Java:** 21
- **Framework:** Spring Boot `4.1.1`
- **Port:** `8081` (`http://localhost:8081`)
- **Database:** In-Memory H2 Database (`jdbc:h2:mem:productdb`)
- **Xác thực:** Ủy quyền xác thực JWT qua Service Bài 2 (`http://localhost:8080/auth`)
- **API Docs:** SpringDoc OpenAPI 3 (Swagger UI)

---

## Hướng dẫn chạy dự án

Chạy đồng thời **Service Bài 2** (Port `8080`) và **Service Bài 3** (Port `8081`):

```powershell
# 1. Chạy Auth Service (Bài 2 - Port 8080)
cd D:\kien_truc_huong_dich_vu\soa-lab2-jwt-auth
.\mvnw.cmd spring-boot:run

# 2. Chạy Product Service (Bài 3 - Port 8081)
cd D:\kien_truc_huong_dich_vu\soa-lab3-product-service
.\mvnw.cmd spring-boot:run
```

---

## Công cụ kiểm thử

- **Swagger UI (Bài 3):** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- **H2 Console (Bài 3):** [http://localhost:8081/h2-console](http://localhost:8081/h2-console) (`JDBC URL`: `jdbc:h2:mem:productdb` | `User`: `sa` | `Password`: *trống*)
- **Lấy JWT Token (Bài 2):** Gọi `POST http://localhost:8080/login` với tài khoản `admin` / `123456`, sau đó dán token vào nút **Authorize** trên Swagger UI của Bài 3.

---

## Danh sách API Endpoints

| STT | Phương thức | URL | Mô tả | Quyền truy cập |
| :-: | :-: | :--- | :--- | :-: |
| 1 | `GET` | `/products` | Lấy danh sách tất cả sản phẩm | **Cần JWT Token** |
| 2 | `GET` | `/products/{id}` | Lấy chi tiết một sản phẩm | **Cần JWT Token** |
| 3 | `POST` | `/products` | Thêm một sản phẩm mới | **Cần JWT Token** |
| 4 | `PUT` | `/products/{id}` | Cập nhật thông tin sản phẩm | **Cần JWT Token** |
| 5 | `DELETE` | `/products/{id}` | Xóa một sản phẩm | **Cần JWT Token** |
