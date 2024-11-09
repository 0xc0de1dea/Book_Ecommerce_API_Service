package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    @Query("SELECT bc FROM BookCategory bc WHERE bc.book.name LIKE %:bookName%")
    List<BookCategory> findByBookNameLike(@Param("bookName") String bookName);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.category.name = :categoryName")
    List<BookCategory> findByBookCategory(@Param("categoryName") String categoryName);
}