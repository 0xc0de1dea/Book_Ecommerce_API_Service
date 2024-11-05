package com.example.book_ecommerce_api_service.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private List<BookCategory> bookCategories;

    public void putBookCategory(BookCategory bookCategory){
        this.bookCategories.add(bookCategory);
    }
}
