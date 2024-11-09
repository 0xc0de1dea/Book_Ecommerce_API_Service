package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.type.SortType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookService {
    Book createBook(BookDto bookDto);

    Page<BookDto> searchBookByName(String name, int page, SortType sortType);

    List<BookDto> searchBookByCategory(String[] categories, int page, SortType sortType);
}
