package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {
    Optional<UserOrder> findFirstByOrderByOrderIdDesc();

    List<UserOrder> findByOrderId(Long orderId);

    List<UserOrder> findByUser_Email(String email);
}
