package org.example.web.Repo;

import org.example.web.Models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategory(String category);
}
