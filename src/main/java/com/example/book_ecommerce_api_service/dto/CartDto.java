package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookItem;
import com.example.book_ecommerce_api_service.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private String userEmail;
    private List<BookItemDto> bookItemDtoList;
    private Integer totalPrice;
    private Integer totalAmount;
}
