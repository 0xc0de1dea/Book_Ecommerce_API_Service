package com.example.book_ecommerce_api_service.specification;

import com.example.book_ecommerce_api_service.domain.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> equalsCategory;
}
