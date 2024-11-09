package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.dto.CategoriesBySortDto;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<?> createBook(
            @Valid @RequestBody BookDto bookDto
    ){
        return ResponseEntity.ok(bookService.createBook(bookDto));
    }

    @GetMapping("/name")
    public ResponseEntity<?> searchBookByName(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "sortType") BookSortType sortType
    ){
        return ResponseEntity.ok(bookService.searchBookByName(name, page, sortType));
    }

    @PostMapping("/category")
    public ResponseEntity<?> searchBookByCategory(
            @RequestBody CategoriesBySortDto categoriesBySortDto,
            @RequestParam(name = "page", defaultValue = "0") int page
    ){
        return ResponseEntity.ok(bookService.searchBookByCategory(categoriesBySortDto.getCategories(), page, categoriesBySortDto.getSortType()));
    }
}
