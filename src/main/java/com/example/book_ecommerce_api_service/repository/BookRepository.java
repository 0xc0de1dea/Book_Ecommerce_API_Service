package com.example.book_ecommerce_api_service.repository;

import com.example.book_ecommerce_api_service.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByName(String name);

    Optional<Book> findByName(String name);

    Page<Book> findByNameLike(String name, Pageable pageable);
}
