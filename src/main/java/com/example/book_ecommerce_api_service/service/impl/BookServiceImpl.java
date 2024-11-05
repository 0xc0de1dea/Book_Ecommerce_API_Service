package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import com.example.book_ecommerce_api_service.domain.Category;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.CategoryRepository;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import com.example.book_ecommerce_api_service.type.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Book createBook(BookDto bookDto){
        if (bookRepository.existsByName(bookDto.getName())){
            throw new CustomException(ErrorCode.ALREADY_EXISTS_BOOK);
        }

        Book book = Book.builder()
                .name(bookDto.getName())
                .price(bookDto.getPrice())
                .description(bookDto.getDescription())
                .amount(bookDto.getAmount())
                .status(bookDto.getStatus())
                .reviews(new ArrayList<>())
                .registerDttm(bookDto.getRegisterDttm())
                .build();

        for (Category category : bookDto.getCategories()) {
            book.putBookCategory(BookCategory.builder()
                    .book(book)
                    .category(category)
                    .build());
        }

        Book ret = bookRepository.save(book);

        for (Category category : bookDto.getCategories()){
            Category findCategory = categoryRepository.findByName(category.getName())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

            findCategory.putBookCategory(BookCategory.builder()
                    .book(book)
                    .category(findCategory)
                    .build());

            categoryRepository.save(findCategory);
        }

        return ret;
    }

    public Page<BookDto> searchBookByName(String name, int page, SortType sortType){
        Pageable pageable = null;

        if (sortType == SortType.ABC_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("name").ascending());
        } else if (sortType == SortType.ABC_DES){
            pageable = PageRequest.of(page, 15, Sort.by("name").descending());
        } else if (sortType == SortType.PRICE_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("price").ascending());
        } else if (sortType == SortType.PRICE_DES){
            pageable = PageRequest.of(page, 15, Sort.by("price").descending());
        }

        Page<Book> book = bookRepository.findByNameLike(name, pageable);

        if (book.getContent().isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }

        return BookDto.fromPageBookEntity(book);
    }

    public List<BookDto> searchBookByCategory(String[] categories, int page, SortType sortType){
        HashMap<String, Integer> countingMap = new HashMap<>();
        HashMap<String, BookCategory> bookCategoryMap = new HashMap<>();

        for (String categoryName : categories){
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

            for (BookCategory bookCategory : category.getBookCategories()){
                String bookName = bookCategory.getBook().getName();
                countingMap.put(bookName, countingMap.getOrDefault(bookName, 0) + 1);
                bookCategoryMap.put(bookName, bookCategory);
            }
        }

        List<Book> bookList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : countingMap.entrySet()){
            if (entry.getValue() == categories.length){
                bookList.add(bookCategoryMap.get(entry.getKey()).getBook());
            }
        }

        if (sortType == SortType.ABC_ASC){
            bookList.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortType == SortType.ABC_DES){
            bookList.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else if (sortType == SortType.PRICE_ASC){
            bookList.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o1.getPrice().compareTo(o2.getPrice());
                }
            });
        } else if (sortType == SortType.PRICE_DES){
            bookList.sort(new Comparator<Book>() {
                @Override
                public int compare(Book o1, Book o2) {
                    return o2.getPrice().compareTo(o1.getPrice());
                }
            });
        }

        List<Book> pagedBook = new ArrayList<>();
        final int PAGE_SIZE = 15;

        for (int i = PAGE_SIZE * page; i < PAGE_SIZE * (page + 1); i++) {
            if (bookList.size() <= i){
                break;
            }

            pagedBook.add(bookList.get(i));
        }


        return BookDto.fromListBookEntity(pagedBook);
    }
}
