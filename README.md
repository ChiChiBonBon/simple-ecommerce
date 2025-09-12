# simple-ecommerce

## 專案配置
- Spring Boot: 3.5.5  
- Java: 17  
- MySQL: 8.0.22  
- Maven: 3.9.11  

---

## 功能說明

### 會員功能
- 註冊 / 登入（UserRegisterRequestDTO、UserLoginRequestDTO、UserResponseDTO）
- 使用 DTO 分層，避免 Entity 直接暴露  

### 商品管理 (Product)
- 商品查詢、條件篩選 (ProductQueryParams)
- 商品新增、修改、刪除 (ProductRequestDTO)
- 分頁查詢 (PageResponseDTO)

### 訂單管理 (Order & OrderItem)
- 建立訂單 (CreateOrderRequestDTO、BuyItemDTO)
- 查詢訂單 / 訂單
- Order 與 OrderItem 使用 JPA 關聯 (OneToMany)

### Repository 實作
- 使用 RowMapper 搭配 JDBC Template 進行查詢
- Repository + Impl 分層

### Service 層
- OrderService / ProductService / UserService 與 Impl 分離
- 確保介面與實作解耦，方便測試與維護

### Controller 層
- RESTful API 設計 (UserController、ProductController、OrderController)
- 統一回傳格式 (PageResponseDTO)

Swagger UI :http://localhost:8080/swagger-ui/index.html<br> 