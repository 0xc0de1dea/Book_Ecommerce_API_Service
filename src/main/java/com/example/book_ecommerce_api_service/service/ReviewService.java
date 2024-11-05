package com.example.book_ecommerce_api_service.service;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.dto.ReviewDto;
import com.example.book_ecommerce_api_service.type.SortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Review createReview(ReviewDto reviewDto);

    Page<ReviewDto.Response> searchReview(String bookName, int page, SortType sortType);
}
