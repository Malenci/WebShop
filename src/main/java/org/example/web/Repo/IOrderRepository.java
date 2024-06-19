package org.example.web.Repo;

import org.example.web.Models.OrderModel;
import org.example.web.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderModel, Long> {

    List<OrderModel> findAllByUser(UserModel user);
}
