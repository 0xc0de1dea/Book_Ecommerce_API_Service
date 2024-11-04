package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import com.example.book_ecommerce_api_service.domain.Category;
import com.example.book_ecommerce_api_service.domain.Review;
import com.example.book_ecommerce_api_service.type.BookStatus;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private String name;
    private Integer price;
    private String description;
    private Integer amount;
    private BookStatus status;
    private List<Category> categories;
    private List<Review> reviews;
    private LocalDateTime registerDttm;

    public Book toEntity(){
        return Book.builder()
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .amount(this.amount)
                .status(this.status)
                .registerDttm(this.registerDttm)
                .build();
    }

    public static Page<BookDto> fromPageBookEntity(Page<Book> pageBook) {
        return pageBook.map(m -> BookDto.builder()
                .name(m.getName())
                .price(m.getPrice())
                .description(m.getDescription())
                .amount(m.getAmount())
                .status(m.getStatus())
                .categories(m.getBookCategories().stream().map(BookCategory::getCategory).toList())
                .registerDttm(LocalDateTime.now())
                .build());
    }

    public static List<BookDto> fromListBookEntity(List<Book> listBook) {
        return listBook.stream().map(m -> BookDto.builder()
                .name(m.getName())
                .price(m.getPrice())
                .description(m.getDescription())
                .amount(m.getAmount())
                .status(m.getStatus())
                .categories(m.getBookCategories().stream().map(BookCategory::getCategory).toList())
                .registerDttm(LocalDateTime.now())
                .build())
                .toList();
    }
}
