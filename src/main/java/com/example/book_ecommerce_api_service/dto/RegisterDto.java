package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.User;
import com.example.book_ecommerce_api_service.type.UserType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RegisterDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank
        private String name;

        @Email
        @NotBlank
        private String email;

        @NotBlank
        @Min(8)
        @Max(100)
        private String password;

        @NotBlank
        @Min(8)
        @Max(100)
        private String confirmPassword;

        @NotBlank
        private String address;

        @NotBlank
        @Min(11)
        @Max(12)
        private String phone;

        @NotNull
        private UserType type;

        public User toUserEntity(){
            return User.builder()
                    .name(this.name)
                    .email(this.email)
                    .password(this.password)
                    .address(this.address)
                    .phone(this.phone)
                    .type(this.type)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class Response {
        private String name;
        private String email;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime registerDateTime;
    }
}
