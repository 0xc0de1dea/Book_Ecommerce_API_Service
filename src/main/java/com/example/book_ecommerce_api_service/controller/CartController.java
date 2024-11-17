package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> addCart(
            @RequestParam Long bookId,
            @RequestParam int amount,
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(cartService.addCart(bookId, amount, token));
    }

    @PutMapping
    public ResponseEntity<?> updateCart(
            @RequestParam Long cartId,
            @RequestParam int amount,
            @RequestHeader("Authorization") String token

    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(cartService.updateCart(cartId, amount, token));
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> deleteCart(
            @PathVariable Long cartId,
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        cartService.deleteCart(cartId, token);

        return ResponseEntity.ok("해당 품목이 장바구니에서 삭제되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> searchCart(
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(cartService.searchCart(token));
    }
}
