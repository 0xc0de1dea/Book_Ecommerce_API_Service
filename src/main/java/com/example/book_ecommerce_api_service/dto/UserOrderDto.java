package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.BookItem;
import com.example.book_ecommerce_api_service.domain.UserOrder;
import com.example.book_ecommerce_api_service.type.OrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDto {
    private Long orderId;
    private String userEmail;
    private List<BookItemDto> bookItemList;
    private OrderStatus orderStatus;
    private LocalDateTime orderDateTime;
}
