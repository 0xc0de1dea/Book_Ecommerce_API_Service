package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.RegisterDto;
import com.example.book_ecommerce_api_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto.Request request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok("이메일 전송 성공");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("이메일 전송 실패");
        }
    }

    @PutMapping("/verification")
    public ResponseEntity<?> emailCheck(@RequestParam("email") String email, @RequestParam("inputUUID") String inputUUID) {
        boolean isMatch = userService.checkVerificationUUID(email, inputUUID);

        return ResponseEntity.ok(isMatch);
    }
}
