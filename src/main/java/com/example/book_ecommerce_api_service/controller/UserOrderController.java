package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.UserOrderDto;
import com.example.book_ecommerce_api_service.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class UserOrderController {
    private final UserOrderService userOrderService;

    @PostMapping
    public ResponseEntity<?> completeOrder(
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(userOrderService.completeOrder(token));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "orderId") Long orderId
    ){
        token = token.replace("Bearer ", "").trim();

        userOrderService.cancelOrder(orderId, token);

        return ResponseEntity.ok("주문 취소가 완료되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> searchOrder(
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(userOrderService.searchOrder(token));
    }
}
