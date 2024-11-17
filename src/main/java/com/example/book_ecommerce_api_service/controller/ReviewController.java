package com.example.book_ecommerce_api_service.controller;

import com.example.book_ecommerce_api_service.dto.ReviewDto;
import com.example.book_ecommerce_api_service.service.ReviewService;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(
            @Valid @RequestBody ReviewDto.Request request,
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(reviewService.createReview(request, token));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @Valid @RequestBody ReviewDto.Request request,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        return ResponseEntity.ok(reviewService.updateReview(request, reviewId, token));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String token
    ){
        token = token.replace("Bearer ", "").trim();

        reviewService.deleteReview(reviewId, token);

        return ResponseEntity.ok("리뷰를 삭제했습니다.");
    }

    @GetMapping
    public ResponseEntity<?> searchReview(
            @RequestParam String bookName,
            @RequestParam int page,
            @RequestParam ReviewSortType sortType
    ){
        return ResponseEntity.ok(reviewService.searchReview(bookName, page, sortType));
    }
}
