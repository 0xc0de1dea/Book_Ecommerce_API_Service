package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.dto.ReviewDto;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import org.springframework.data.domain.Page;

public interface ReviewService {
    ReviewDto.Response createReview(ReviewDto.Request request, String token);

    ReviewDto.Response updateReview(ReviewDto.Request request, Long reviewId, String token);

    void deleteReview(Long reviewId, String token);

    Page<ReviewDto.Response> searchReview(String bookName, int page, ReviewSortType sortType);
}
