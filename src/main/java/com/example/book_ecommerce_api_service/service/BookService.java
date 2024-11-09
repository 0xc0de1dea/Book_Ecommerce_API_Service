package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto bookDto);

    List<BookDto> searchBookByName(String name, int page, BookSortType sortType);

    List<BookDto> searchBookByCategory(String[] categories, int page, BookSortType sortType);
}
