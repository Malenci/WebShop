package org.example.web.Controllers;

import org.example.web.Models.CategoryModel;
import org.example.web.Models.ProductModel;
import org.example.web.Models.UserModel;
import org.example.web.Repo.ICategoryRepository;
import org.example.web.Repo.IProductRepository;
import org.example.web.Repo.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IProductRepository productRepository;

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "admin";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam("name") String name,
                              @RequestParam("description") String description,
                              Model model) {
        CategoryModel category = new CategoryModel();
        category.setName(name);
        category.setDescription(description);
        categoryRepository.save(category);
        return "redirect:/admin";
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam("name") String name,
                             @RequestParam("brand") String brand,
                             @RequestParam("price") BigDecimal price,
                             @RequestParam("category") String category,
                             @RequestParam("size") String size,
                             @RequestParam("color") String color,
                             Model model) {
        ProductModel product = new ProductModel();
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(price);
        product.setCategory(category);
        product.setSize(size);
        product.setColor(color);
        productRepository.save(product);
        return "redirect:/admin";
    }


    @PostMapping("/findUser")
    public String findUser(@RequestParam("username") String username, Model model) {
        UserModel user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "admin";
    }
}
