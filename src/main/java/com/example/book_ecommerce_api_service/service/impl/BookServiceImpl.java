package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import com.example.book_ecommerce_api_service.domain.Category;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.repository.BookCategoryRepository;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.CategoryRepository;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import com.example.book_ecommerce_api_service.type.ReviewSortType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Transactional
    public BookDto createBook(BookDto bookDto){
        if (bookRepository.existsByName(bookDto.getName())){
            throw new CustomException(ErrorCode.ALREADY_EXISTS_BOOK);
        }

        Book book = bookDto.toEntity();
        book = bookRepository.save(book);

        for (String categoryName : bookDto.getCategories()){
            Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);

            if (optionalCategory.isPresent()){
                BookCategory bookCategory = BookCategory.builder()
                        .book(book)
                        .category(optionalCategory.get())
                        .build();

                bookCategoryRepository.save(bookCategory);
            } else {
                Category category = categoryRepository.save(Category.builder()
                        .name(categoryName)
                        .build());

                BookCategory bookCategory = BookCategory.builder()
                        .book(book)
                        .category(category)
                        .build();

                bookCategoryRepository.save(bookCategory);
            }
        }

        bookDto.setRegisterDateTime(book.getRegisterDateTime());

        return bookDto;
    }

    public List<BookDto> searchBookByName(String name, int page, BookSortType sortType){
        List<BookCategory> bookCategoryList = bookCategoryRepository.findByBookNameLike(name);

        if (bookCategoryList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }

        HashMap<String, List<String>> bookMap = new HashMap<>();

        for (BookCategory bookCategory : bookCategoryList){
            String bookName = bookCategory.getBook().getName();

            if (bookMap.containsKey(bookName)){
                bookMap.get(bookName).add(bookCategory.getCategory().getName());
            } else {
                List<String> bookList = new ArrayList<>();
                bookList.add(bookCategory.getCategory().getName());
                bookMap.put(bookName, bookList);
            }
        }

        List<BookDto> bookDtoList = new ArrayList<>();

        for (BookCategory bookCategory : bookCategoryList){
            if (bookMap.containsKey(bookCategory.getBook().getName())){
                BookDto bookDto = BookDto.fromEntity(bookCategory.getBook());
                bookDto.setCategories(bookMap.get(bookCategory.getBook().getName()));
                bookDtoList.add(bookDto);
            }
        }

        sortBySortType(bookDtoList, sortType);

        return bookDtoList;
    }

    public List<BookDto> searchBookByCategory(String[] categories, int page, BookSortType sortType){
        HashMap<String, Integer> countingMap = new HashMap<>();
        HashMap<String, List<String>> bookMap = new HashMap<>();
        Set<BookCategory> bookCategorySet = new HashSet<>();

        if (categories.length == 0){
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        for (String categoryName : categories){
            List<BookCategory> bookCategoryList = bookCategoryRepository.findByBookCategory(categoryName);

            if (bookCategoryList.isEmpty()){
                throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
            }

            bookCategorySet.addAll(bookCategoryList);

            for (BookCategory bookCategory : bookCategoryList){
                String bookName = bookCategory.getBook().getName();

                if (bookMap.containsKey(bookName)){
                    bookMap.get(bookName).add(bookCategory.getCategory().getName());
                    countingMap.put(bookName, countingMap.getOrDefault(bookName, 0) + 1);
                } else {
                    List<String> bookList = new ArrayList<>();
                    bookList.add(bookCategory.getCategory().getName());
                    bookMap.put(bookName, bookList);
                    countingMap.put(bookName, countingMap.getOrDefault(bookName, 0) + 1);
                }
            }
        }

        List<BookDto> bookDtoList = new ArrayList<>();

        for (BookCategory bookCategory : bookCategorySet){
            String bookName = bookCategory.getBook().getName();

            if (bookMap.containsKey(bookName) && countingMap.get(bookName) == categories.length){
                BookDto bookDto = BookDto.fromEntity(bookCategory.getBook());
                bookDto.setCategories(bookMap.get(bookCategory.getBook().getName()));
                bookDtoList.add(bookDto);
            }
        }

        sortBySortType(bookDtoList, sortType);

        return getPagedBook(bookDtoList, page);
    }

    private static void sortBySortType(List<BookDto> bookDtoList, BookSortType sortType){
        if (sortType == BookSortType.ABC_ASC){
            bookDtoList.sort(new Comparator<BookDto>() {
                @Override
                public int compare(BookDto o1, BookDto o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortType == BookSortType.ABC_DES){
            bookDtoList.sort(new Comparator<BookDto>() {
                @Override
                public int compare(BookDto o1, BookDto o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else if (sortType == BookSortType.PRICE_ASC){
            bookDtoList.sort(new Comparator<BookDto>() {
                @Override
                public int compare(BookDto o1, BookDto o2) {
                    return o1.getPrice().compareTo(o2.getPrice());
                }
            });
        } else if (sortType == BookSortType.PRICE_DES){
            bookDtoList.sort(new Comparator<BookDto>() {
                @Override
                public int compare(BookDto o1, BookDto o2) {
                    return o2.getPrice().compareTo(o1.getPrice());
                }
            });
        }
    }

    private static List<BookDto> getPagedBook(List<BookDto> bookDtoList, int page){
        List<BookDto> pagedBook = new ArrayList<>();
        final int PAGE_SIZE = 15;

        for (int i = PAGE_SIZE * page; i < PAGE_SIZE * (page + 1); i++) {
            if (bookDtoList.size() <= i){
                break;
            }

            pagedBook.add(bookDtoList.get(i));
        }

        return bookDtoList;
    }

    private static Pageable getPageable(int page, BookSortType sortType) {
        Pageable pageable = null;

        if (sortType == BookSortType.ABC_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("name").ascending());
        } else if (sortType == BookSortType.ABC_DES){
            pageable = PageRequest.of(page, 15, Sort.by("name").descending());
        } else if (sortType == BookSortType.PRICE_ASC){
            pageable = PageRequest.of(page, 15, Sort.by("price").ascending());
        } else if (sortType == BookSortType.PRICE_DES){
            pageable = PageRequest.of(page, 15, Sort.by("price").descending());
        }

        return pageable;
    }
}
