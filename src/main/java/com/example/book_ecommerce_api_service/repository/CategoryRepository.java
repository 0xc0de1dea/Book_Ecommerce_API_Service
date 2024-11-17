package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}
