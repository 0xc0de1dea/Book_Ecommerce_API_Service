package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookItem;
import com.example.book_ecommerce_api_service.dto.BookItemDto;
import com.example.book_ecommerce_api_service.dto.CartDto;

import java.util.List;

public interface CartService {
    BookItemDto addCart(Long bookId, int amount, String token);

    BookItemDto updateCart(Long cartId, int amount, String token);

    void deleteCart(Long cartId, String token);

    CartDto searchCart(String token);
}
