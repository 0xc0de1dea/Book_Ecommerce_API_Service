package com.example.book_ecommerce_api_service.domain;

import com.example.book_ecommerce_api_service.converter.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    @Convert(converter = StringListConverter.class)
    private List<BookCategory> bookCategories;

    public void putBookCategory(BookCategory bookCategory){
        if (this.bookCategories == null) {
            this.bookCategories = new ArrayList<>();
        }

        this.bookCategories.add(bookCategory);
    }
}
