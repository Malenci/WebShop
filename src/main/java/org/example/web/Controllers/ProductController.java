package org.example.web.Controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.web.Models.CartModel;
import org.example.web.Models.ProductModel;
import org.example.web.Models.UserModel;
import org.example.web.Repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller

public class ProductController {

    private final IProductRepository productRepository;

    @Autowired
    public ProductController(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/catalog")
    public String getAllProducts(Model model) {
        List<ProductModel> products = productRepository.findAll(Pageable.ofSize(10)).getContent();;
        model.addAttribute("products", products);
        return "catalog";
    }

    @GetMapping("/catalog/category{category}")
    public String getProductsByCategory(@RequestParam(required = false) @PathVariable String category, Model model) {
        List<ProductModel> products;
        if (category != null) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        return "showProduct";
    }

    @GetMapping("/catalog/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new ProductModel());
        return "newProduct";
    }

    @PostMapping("/catalog/new")
    public String addProduct(@ModelAttribute ProductModel product, Model model) {
        productRepository.save(product);
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            ProductModel product = productOptional.get();
            model.addAttribute("product", product);
            return "showProduct";
        } else {
            return "home";
        }
    }


    @GetMapping("/catalog/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        productOptional.ifPresent(product -> model.addAttribute("product", product));
        return "editProduct";
    }

    @PostMapping("/catalog/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductModel product, Model model) {
        product.setId(id);
        productRepository.save(product);
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/{id}/delete")
    public String deleteProduct(@PathVariable Long id, Model model) {
        productRepository.deleteById(id);
        return "redirect:/catalog";
    }
}