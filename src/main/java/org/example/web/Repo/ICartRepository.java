package org.example.web.Repo;

import org.example.web.Models.CartModel;
import org.example.web.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartModel, Long> {
    CartModel findFirstByUser(UserModel user);
    CartModel findByUserId(Long userId);

    CartModel findByUser(UserModel user);

}