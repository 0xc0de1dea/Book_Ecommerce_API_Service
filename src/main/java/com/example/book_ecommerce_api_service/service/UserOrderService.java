package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.dto.UserOrderDto;

import java.util.List;

public interface UserOrderService {
    UserOrderDto completeOrder(String token);

    void cancelOrder(Long orderId, String token);

    List<UserOrderDto> searchOrder(String token);
}
