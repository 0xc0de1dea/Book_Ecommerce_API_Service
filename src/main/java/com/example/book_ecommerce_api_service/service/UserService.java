package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.dto.RegisterDto;

public interface UserService {
    void registerUser(RegisterDto.Request registerDto);

    String getVerificationEmail(String email);

    boolean checkVerificationUUID(String email, String inputUUID);
}
