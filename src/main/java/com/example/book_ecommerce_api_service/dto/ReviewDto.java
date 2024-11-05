package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
        private Integer rating;

        @NotEmpty
        private String comment;

        @NotBlank
        private Long book_id;
    }

    @Getter
    @Builder
    public static class Response {
        private String userName;
        private String bookName;
        private Integer rating;
        private String comment;
        private Integer like;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime registerDttm;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime updateDttm;
    }

    public static Page<ReviewDto.Response> fromPageReviewEntity(Page<Review> pageReview) {
        return pageReview.map(m -> ReviewDto.Response.builder()
                .userName(m.getUser().getName())
                .bookName(m.getBook().getName())
                .rating(m.getRating())
                .comment(m.getComment())
                .like(m.getReviewLike())
                .build());
    }
}