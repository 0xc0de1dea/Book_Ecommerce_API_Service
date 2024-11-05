package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.type.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LoginDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Min(value = 8)
        @Max(value = 100)
        private String password;
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String email;
        private String name;
        private UserType type;
    }
}
