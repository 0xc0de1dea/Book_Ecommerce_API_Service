package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookCategory;
import com.example.book_ecommerce_api_service.domain.Category;
import com.example.book_ecommerce_api_service.dto.BookDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.jwt.JWTUtil;
import com.example.book_ecommerce_api_service.repository.BookCategoryRepository;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.CategoryRepository;
import com.example.book_ecommerce_api_service.service.BookService;
import com.example.book_ecommerce_api_service.type.BookSortType;
import com.example.book_ecommerce_api_service.type.BookStatus;
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
    private final JWTUtil jwtUtil;

    @Transactional
    public BookDto.Response createBook(BookDto.Request request, String token){
        if (bookRepository.existsByName(request.getName())){
            throw new CustomException(ErrorCode.ALREADY_EXISTS_BOOK);
        }

        String userEmail = jwtUtil.getEmail(token);

        Book book = request.toEntity();
        book.setSeller(userEmail);
        book.setRegisterDateTime(LocalDateTime.now());
        book = bookRepository.save(book);

        for (String categoryName : request.getCategories()){
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

        BookDto.Response response = BookDto.Response.builder()
                .name(book.getName())
                .price(book.getPrice())
                .description(book.getDescription())
                .amount(book.getAmount())
                .seller(book.getSeller())
                .status(book.getStatus())
                .categories(request.getCategories())
                .registerDateTime(book.getRegisterDateTime())
                .build();

        return response;
    }

    @Transactional
    public void updateBook(Long bookId, Integer amount, BookStatus bookStatus, String token){
        String userEmail = jwtUtil.getEmail(token);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        if (!book.getSeller().equals(userEmail)){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        book.setAmount(amount);

        if (amount <= 0){
            if (bookStatus == BookStatus.NOT_AVAILABLE){
                book.setStatus(BookStatus.NOT_AVAILABLE);
            } else {
                book.setStatus(BookStatus.SOLD_OUT);
            }
        } else if (amount > 0 && bookStatus == BookStatus.NOT_AVAILABLE){
            book.setStatus(BookStatus.NOT_AVAILABLE);
        }

        bookRepository.save(book);
    }

    public List<BookDto.Response> searchBookByName(String name, int page, BookSortType sortType){
        List<BookCategory> bookCategoryList = bookCategoryRepository.findByBookNameLike(name);

        if (bookCategoryList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }

        HashMap<String, List<String>> bookMap = new HashMap<>();
        HashSet<Book> bookSet = new HashSet<>();

        for (BookCategory bookCategory : bookCategoryList){
            String bookName = bookCategory.getBook().getName();

            if (bookMap.containsKey(bookName)){
                bookMap.get(bookName).add(bookCategory.getCategory().getName());
            } else {
                List<String> bookList = new ArrayList<>();
                bookList.add(bookCategory.getCategory().getName());
                bookMap.put(bookName, bookList);
            }

            bookSet.add(bookCategory.getBook());
        }

        List<BookDto.Response> bookDtoList = new ArrayList<>();

        for (Book book : bookSet){
            if (bookMap.containsKey(book.getName())){
                BookDto.Response response = BookDto.Response.fromEntity(book);
                response.setCategories(bookMap.get(book.getName()));
                bookDtoList.add(response);
            }
        }

        sortBySortType(bookDtoList, sortType);

        return bookDtoList;
    }

    public List<BookDto.Response> searchBookByCategory(String[] categories, int page, BookSortType sortType){
        HashMap<String, Integer> countingMap = new HashMap<>();
        Set<BookCategory> bookCategorySet = new HashSet<>();

        if (categories.length == 0){
            throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
        }

        for (String categoryName : categories){
            List<BookCategory> bookCategoryList = bookCategoryRepository.findByCategoryName(categoryName);

            if (bookCategoryList.isEmpty()){
                throw new CustomException(ErrorCode.NOT_FOUND_CATEGORY);
            }

            bookCategorySet.addAll(bookCategoryList);

            for (BookCategory bookCategory : bookCategoryList){
                String bookName = bookCategory.getBook().getName();
                countingMap.put(bookName, countingMap.getOrDefault(bookName, 0) + 1);
            }
        }

        List<BookDto.Response> bookDtoList = new ArrayList<>();

        for (BookCategory bookCategory : bookCategorySet){
            String bookName = bookCategory.getBook().getName();

            if (countingMap.containsKey(bookName) && countingMap.get(bookName) == categories.length){
                BookDto.Response response = BookDto.Response.fromEntity(bookCategory.getBook());

                List<BookCategory> bookCategoryList = bookCategoryRepository.findByBookName(bookName);

                List<String> categoryList = new ArrayList<>();

                for (BookCategory bc : bookCategoryList){
                    categoryList.add(bc.getCategory().getName());
                }

                response.setCategories(categoryList);
                bookDtoList.add(response);
            }
        }

        sortBySortType(bookDtoList, sortType);

        return getPagedBook(bookDtoList, page);
    }

    private static void sortBySortType(List<BookDto.Response> bookDtoList, BookSortType sortType){
        if (sortType == BookSortType.ABC_ASC){
            bookDtoList.sort(new Comparator<BookDto.Response>() {
                @Override
                public int compare(BookDto.Response o1, BookDto.Response o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortType == BookSortType.ABC_DES){
            bookDtoList.sort(new Comparator<BookDto.Response>() {
                @Override
                public int compare(BookDto.Response o1, BookDto.Response o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        } else if (sortType == BookSortType.PRICE_ASC){
            bookDtoList.sort(new Comparator<BookDto.Response>() {
                @Override
                public int compare(BookDto.Response o1, BookDto.Response o2) {
                    return o1.getPrice().compareTo(o2.getPrice());
                }
            });
        } else if (sortType == BookSortType.PRICE_DES){
            bookDtoList.sort(new Comparator<BookDto.Response>() {
                @Override
                public int compare(BookDto.Response o1, BookDto.Response o2) {
                    return o2.getPrice().compareTo(o1.getPrice());
                }
            });
        }
    }

    private static List<BookDto.Response> getPagedBook(List<BookDto.Response> bookDtoList, int page){
        List<BookDto.Response> pagedBook = new ArrayList<>();
        final int PAGE_SIZE = 15;

        for (int i = PAGE_SIZE * page; i < PAGE_SIZE * (page + 1); i++) {
            if (bookDtoList.size() <= i){
                break;
            }

            pagedBook.add(bookDtoList.get(i));
        }

        return pagedBook;
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
