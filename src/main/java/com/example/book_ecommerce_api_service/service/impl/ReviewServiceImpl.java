package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.domain.User;
import com.example.book_ecommerce_api_service.dto.ReviewDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.jwt.JWTUtil;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.ReviewRepository;
import com.example.book_ecommerce_api_service.repository.UserRepository;
import com.example.book_ecommerce_api_service.service.ReviewService;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public ReviewDto.Response createReview(ReviewDto.Request request, String token){
        String userEmail = jwtUtil.getEmail(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        Review review = Review.builder()
                .user(user)
                .book(book)
                .rating(request.getRating())
                .comment(request.getComment())
                .reviewLike(0)
                .registerDateTime(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        return ReviewDto.Response.builder()
                .userEmail(userEmail)
                .bookName(book.getName())
                .rating(request.getRating())
                .comment(request.getComment())
                .like(0)
                .registerDateTime(LocalDateTime.now())
                .build();
    }

    @Transactional
    public ReviewDto.Response updateReview(ReviewDto.Request request, Long reviewId, String token){
        String userEmail = jwtUtil.getEmail(token);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!review.getUser().getEmail().equals(userEmail)){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdateDateTime(LocalDateTime.now());

        reviewRepository.save(review);

        return ReviewDto.Response.builder()
                .userEmail(userEmail)
                .bookName(review.getBook().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .like(review.getReviewLike())
                .registerDateTime(review.getRegisterDateTime())
                .updateDateTime(review.getUpdateDateTime())
                .build();
    }

    @Transactional
    public void deleteReview(Long reviewId, String token){
        String userEmail = jwtUtil.getEmail(token);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!review.getBook().getSeller().equals(userEmail) &&
        !review.getUser().getEmail().equals(userEmail)){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        reviewRepository.delete(review);
    }

    public Page<ReviewDto.Response> searchReview(String name, int page, ReviewSortType sortType){
        Optional<Book> book = bookRepository.findByName(name);

        if (book.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }

        Pageable pageable = getPageable(page, sortType);

        Page<Review> reviews = reviewRepository.findByBook_Name(name, pageable);

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
