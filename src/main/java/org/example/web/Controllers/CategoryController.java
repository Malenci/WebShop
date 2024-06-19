package org.example.web.Controllers;

import org.example.web.Models.CategoryModel;
import org.example.web.Models.ProductModel;
import org.example.web.Repo.ICategoryRepository;
import org.example.web.Repo.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

public class CategoryController {

    @Autowired

    private ICategoryRepository iCategoryRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductRepository iProductRepository;

    @GetMapping("/category")
    public String getAllCategories(Model model) {
        List<CategoryModel> categories = iCategoryRepository.findAll(Pageable.ofSize(10)).getContent();
        model.addAttribute("categories", categories);
        return "category";
    }
    @GetMapping("/categoryShow/{id}")
    public String findByCategory(@PathVariable String id, Model model) {
        try {
            long categoryId = Long.parseLong(id);
            Optional<CategoryModel> category = iCategoryRepository.findById(categoryId);


                List<ProductModel> products = productRepository.findByCategory(String.valueOf(category));
                model.addAttribute("category", category);
                model.addAttribute("products", products);
                return "categoryShow";

        } catch (NumberFormatException e) {
            return "redirect:/category";
        }
    }

}