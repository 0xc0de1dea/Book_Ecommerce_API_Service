package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.dto.CategoriesBySortDto;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.BookStatus;
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
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody BookDto.Request request
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(bookService.createBook(request, token));
    }

    @PutMapping
    public ResponseEntity<?> updateBook(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "bookId") Long bookId,
            @RequestParam(name = "amount") Integer amount,
            @RequestParam(name = "status") BookStatus status
            ){
        token = token.replace("Bearer ", "").trim();

        bookService.updateBook(bookId, amount, status, token);

        return ResponseEntity.ok("등록되어 있는 책을 업데이트 하였습니다.");
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
            @Valid @RequestBody CategoriesBySortDto categoriesBySortDto,
            @RequestParam(name = "page", defaultValue = "0") int page
    ){
        return ResponseEntity.ok(bookService.searchBookByCategory(categoriesBySortDto.getCategories(), page, categoriesBySortDto.getSortType()));
    }
}
