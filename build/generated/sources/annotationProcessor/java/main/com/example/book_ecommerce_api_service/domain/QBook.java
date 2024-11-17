package com.example.book_ecommerce_api_service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -858241327L;

    public static final QBook book = new QBook("book");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final ListPath<BookCategory, QBookCategory> bookCategories = this.<BookCategory, QBookCategory>createList("bookCategories", BookCategory.class, QBookCategory.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> registerDateTime = createDateTime("registerDateTime", java.time.LocalDateTime.class);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final StringPath seller = createString("seller");

    public final EnumPath<com.example.book_ecommerce_api_service.type.BookStatus> status = createEnum("status", com.example.book_ecommerce_api_service.type.BookStatus.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

