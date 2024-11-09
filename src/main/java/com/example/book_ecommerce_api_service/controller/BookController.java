package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.SortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<?> createBook(
            @Valid @RequestBody BookDto bookDto
    ){
        return ResponseEntity.ok(bookService.createBook(bookDto));
    }

    @GetMapping("/search/name")
    public ResponseEntity<?> searchBookByName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestBody SortType sortType
    ){
        return ResponseEntity.ok(bookService.searchBookByName(name, page, sortType));
    }

    @GetMapping("/search/category")
    public ResponseEntity<?> searchBookByCategory(
            @RequestBody String[] categories,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestBody SortType sortType
    ){
        return ResponseEntity.ok(bookService.searchBookByCategory(categories, page, sortType));
    }
}
