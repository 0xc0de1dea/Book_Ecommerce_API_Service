package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.BookStatus;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    BookDto.Response createBook(BookDto.Request bookDto, String token);

    void updateBook(Long bookId, Integer amount, BookStatus bookStatus, String token);

    List<BookDto.Response> searchBookByName(String name, int page, BookSortType sortType);

    List<BookDto.Response> searchBookByCategory(String[] categories, int page, BookSortType sortType);
}
