package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.BookItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookItemRepository extends JpaRepository<BookItem, Long> {
    @Query("SELECT bi FROM BookItem bi WHERE bi.user.name = :userEmail")
    List<BookItem> findByUserEmail(@Param("userEmail") String userEmail);
}
