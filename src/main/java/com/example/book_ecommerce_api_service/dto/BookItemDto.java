package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookItem;
import com.example.book_ecommerce_api_service.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemDto {
    private String userEmail;
    private String bookName;
    private Integer amount;

    public static BookItemDto fromEntity(BookItem bookItem){
        return BookItemDto.builder()
                .userEmail(bookItem.getUser().getEmail())
                .bookName(bookItem.getBook().getName())
                .amount(bookItem.getAmount())
                .build();
    }
}
