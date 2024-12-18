package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import com.example.book_ecommerce_api_service.domain.Category;
import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.type.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public class BookDto {
    @Getter
    @Builder
    public static class Request {
        @NotBlank
        private String name;

        @NotNull
        private Integer price;

        @NotBlank
        private String description;

        @NotNull
        private Integer amount;

        @NotNull
        private BookStatus status;

        @NotNull
        private List<String> categories;

        public Book toEntity(){
            return Book.builder()
                    .name(this.name)
                    .price(this.price)
                    .description(this.description)
                    .amount(this.amount)
                    .status(this.status)
                    .build();
        }
    }

    @Setter
    @Getter
    @Builder
    public static class Response {
        private String name;
        private Integer price;
        private String description;
        private Integer amount;
        private String seller;
        private BookStatus status;
        private List<String> categories;
        private LocalDateTime registerDateTime;

        public static BookDto.Response fromEntity(Book book){
            return Response.builder()
                    .name(book.getName())
                    .price(book.getPrice())
                    .description(book.getDescription())
                    .amount(book.getAmount())
                    .seller(book.getSeller())
                    .status(book.getStatus())
                    .registerDateTime(book.getRegisterDateTime())
                    .build();
        }
    }

//    public static Page<BookDto> fromPageBookEntity(Page<BookCategory> pageBook) {
//        return pageBook.map(m -> BookDto.builder()
//                .name(m.getBook().getName())
//                .price(m.getBook().getPrice())
//                .description(m.getBook().getDescription())
//                .amount(m.getBook().getAmount())
//                .status(m.getBook().getStatus())
//                .categories(m.getBookCategories().stream().map((bookcategory) -> bookcategory.getCategory().getName()).toList())
//                //.categories(m.getBookCategories().stream().map(BookCategory::getCategory).toList())
//                .registerDateTime(LocalDateTime.now())
//                .build());
//    }
//
//    public static List<BookDto> fromListBookEntity(List<Book> listBook) {
//        return listBook.stream().map(m -> BookDto.builder()
//                .name(m.getName())
//                .price(m.getPrice())
//                .description(m.getDescription())
//                .amount(m.getAmount())
//                .status(m.getStatus())
//                .categories(m.getBookCategories().stream().map((bookcategory) -> bookcategory.getCategory().getName()).toList())
//                //.categories(m.getBookCategories().stream().map(BookCategory::getCategory).toList())
//                .registerDateTime(LocalDateTime.now())
//                .build())
//                .toList();
//    }
}
