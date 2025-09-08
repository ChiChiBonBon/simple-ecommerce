package com.changbenny.simpleecommerce.entity;

import com.changbenny.simpleecommerce.constant.ProductCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

//定義ProductEntity
@Entity
@Table(name = "product")
public class ProductEntity {

    //定義Primary key欄位
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="product_id", nullable = false)
    private Integer productId;

    //定義product_name欄位大小長度128
    @Column(name = "product_name",nullable = false,length = 128)
    private String productName;

    //定義category欄位大小長度32
    @Column(name = "category", nullable = false,length = 32)
    @Enumerated(EnumType.STRING)//enum常量的名字存進DB
    private ProductCategory category;

    //定義image_url欄位大小長度256
    @Column(name = "image_url",nullable = false,length = 256)
    private String imageUrl;

    //定義price欄位精度10，小數位2位
    @Column(name = "price",precision = 10,scale=2)
    private BigDecimal price;

    //定義stock欄位
    @Column(name = "stock",nullable = false)
    private Integer stock;

    //定義description欄位長度大小1024
    @Column(name = "description",length = 1024)
    private String description;

    //定義created_date欄位
    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)//存入DB是TIMESTAMP HH:mm:ss
    private Date createdDate;

    //定義last_modified_date欄位
    @Column(name = "last_modified_date",   nullable = false)
    @Temporal(TemporalType.TIMESTAMP)//存入DB是TIMESTAMP HH:mm:ss
    private Date lastModifiedDate;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
