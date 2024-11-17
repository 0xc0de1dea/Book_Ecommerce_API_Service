package com.example.book_ecommerce_api_service.dto;

import com.example.book_ecommerce_api_service.type.BookSortType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesBySortDto {
   private String[] categories;
   private BookSortType sortType;
}
