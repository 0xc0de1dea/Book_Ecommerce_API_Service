package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.RegisterDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto.Request request) {
//        try {
//            CompletableFuture<Void> result =
//            result.join();
            return ResponseEntity.ok(userService.registerUser(request));
//        } catch (CustomException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getErrorMessage());
//        }
    }

    @PutMapping
    public ResponseEntity<?> emailCheck(
            @RequestParam("email") String email, @RequestParam("inputUUID") String inputUUID
    ) {
        boolean isMatch = userService.checkVerificationUUID(email, inputUUID);

        return ResponseEntity.ok(isMatch);
    }
}
