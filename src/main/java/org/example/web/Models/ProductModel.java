package org.example.web.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private String category;
    private String size;
    private String color;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartModel cart;

    public ProductModel() {

    }

    public ProductModel(String name, String brand, BigDecimal price, String category, String size, String color) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.category = category;
        this.size = size;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public CartModel getCart() {
        return cart;
    }

    public void setCart(CartModel cart) {
        this.cart = cart;
    }
}