package org.example.web.Repo;

import org.example.web.Models.CategoryModel;
import org.example.web.Models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoryRepository extends JpaRepository<CategoryModel, Long> {
//    List<ProductModel> getProductsByCategory(String name);
    CategoryModel findByName(String name);
}
