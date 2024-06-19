package org.example.web.Repo;


import org.example.web.Models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByIdIn(List<Long> ids);

}

