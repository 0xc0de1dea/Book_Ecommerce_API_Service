package com.example.book_ecommerce_api_service.exception;

import com.example.book_ecommerce_api_service.type.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String errorMessage;

    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
