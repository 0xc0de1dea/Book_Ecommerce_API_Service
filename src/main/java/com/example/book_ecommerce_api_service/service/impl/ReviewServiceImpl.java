package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.dto.ReviewDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.ReviewRepository;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    public Page<ReviewDto.Response> searchReview(String name, int page, ReviewSortType sortType){
        Book book = bookRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        Pageable pageable = getPageable(page, sortType);

        Page<Review> reviews = reviewRepository.findByBook(book, pageable);

        return ReviewDto.fromPageReviewEntity(reviews);
    }

    private static Pageable getPageable(int page, ReviewSortType sortType) {
        Pageable pageable = null;

        if (sortType == ReviewSortType.RATING_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("rating").ascending());
        } else if (sortType == ReviewSortType.RATING_DES){
            pageable = PageRequest.of(page, 15, Sort.by("rating").descending());
        } else if (sortType == ReviewSortType.LIKE_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("like").ascending());
        } else if (sortType == ReviewSortType.LIKE_DES){
            pageable = PageRequest.of(page, 15, Sort.by("like").descending());
        } else if (sortType == ReviewSortType.UPDATE_DATE_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("updateDttm").ascending());
        } else if (sortType == ReviewSortType.UPDATE_DATE_DES){
            pageable = PageRequest.of(page, 15, Sort.by("updateDttm").descending());
        }

        return pageable;
    }
}
