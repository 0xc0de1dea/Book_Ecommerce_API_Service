package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.RegisterDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto.Request request) {
        try {
            CompletableFuture<Void> result = userService.registerUser(request);
            result.join();  // 비동기 메서드를 기다림
            return ResponseEntity.ok("회원가입이 완료되었습니다. 이메일 인증을 시도하세요.");
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getErrorMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> emailCheck(@RequestParam("email") String email, @RequestParam("inputUUID") String inputUUID) {
        boolean isMatch = userService.checkVerificationUUID(email, inputUUID);

        return ResponseEntity.ok(isMatch);
    }
}
