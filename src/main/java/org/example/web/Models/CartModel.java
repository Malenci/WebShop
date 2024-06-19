package org.example.web.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserModel user;

    @ManyToMany
    private List<ProductModel> products = new ArrayList<>();

    public CartModel() {
    }

    public CartModel(UserModel user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ProductModel product : products) {
            total = total.add(product.getPrice());
        }
        return total;
    }

    // Метод для очистки корзины
    public void clearCart() {
        products.clear();
    }
}