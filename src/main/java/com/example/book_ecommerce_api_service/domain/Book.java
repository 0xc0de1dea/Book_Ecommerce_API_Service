package com.example.book_ecommerce_api_service.domain;

import com.example.book_ecommerce_api_service.converter.StringListConverter;
import com.example.book_ecommerce_api_service.type.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private String description;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    @OneToMany(mappedBy = "book")
    @Convert(converter = StringListConverter.class)
    private List<BookCategory> bookCategories;

    @OneToMany(mappedBy = "book")
    @Convert(converter = StringListConverter.class)
    private List<Review> reviews;

    @CreatedDate
    private LocalDateTime registerDateTime;

    public void putBookCategory(BookCategory bookCategory){
        if (this.bookCategories == null) {
            this.bookCategories = new ArrayList<>();
        }

        this.bookCategories.add(bookCategory);
    }
}
