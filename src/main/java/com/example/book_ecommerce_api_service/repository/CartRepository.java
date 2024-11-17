package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = :userEmail")
    List<Cart> findByUserEmail(@Param("userEmail") String userEmail);
}
