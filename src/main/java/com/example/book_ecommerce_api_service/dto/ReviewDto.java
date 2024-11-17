package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Review;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ReviewDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Request {
        @Min(value = 1)
        @Max(value = 5)
        @NotNull
        private Integer rating;

        @NotEmpty
        private String comment;

        @NotNull
        private Long bookId;
    }

    @Getter
    @Builder
    public static class Response {
        private String userEmail;
        private String bookName;
        private Integer rating;
        private String comment;
        private Integer like;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime registerDateTime;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime updateDateTime;
    }

    public static Page<ReviewDto.Response> fromPageReviewEntity(Page<Review> pageReview) {
        return pageReview.map(m -> Response.builder()
                .userEmail(m.getUser().getEmail())
                .bookName(m.getBook().getName())
                .rating(m.getRating())
                .comment(m.getComment())
                .like(m.getReviewLike())
                .registerDateTime(m.getRegisterDateTime())
                .updateDateTime(m.getUpdateDateTime())
                .build());
    }
}
