package com.example.book_ecommerce_api_service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookItem is a Querydsl query type for BookItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookItem extends EntityPathBase<BookItem> {

    private static final long serialVersionUID = -2031524476L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookItem bookItem = new QBookItem("bookItem");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final QBook book;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBookItem(String variable) {
        this(BookItem.class, forVariable(variable), INITS);
    }

    public QBookItem(Path<? extends BookItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookItem(PathMetadata metadata, PathInits inits) {
        this(BookItem.class, metadata, inits);
    }

    public QBookItem(Class<? extends BookItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBook(forProperty("book")) : null;
    }

}

