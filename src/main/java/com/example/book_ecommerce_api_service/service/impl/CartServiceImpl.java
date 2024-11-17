package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.Book;
import com.example.book_ecommerce_api_service.domain.BookItem;
import com.example.book_ecommerce_api_service.domain.Cart;
import com.example.book_ecommerce_api_service.domain.User;
import com.example.book_ecommerce_api_service.dto.BookItemDto;
import com.example.book_ecommerce_api_service.dto.CartDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.jwt.JWTUtil;
import com.example.book_ecommerce_api_service.repository.BookItemRepository;
import com.example.book_ecommerce_api_service.repository.BookRepository;
import com.example.book_ecommerce_api_service.repository.CartRepository;
import com.example.book_ecommerce_api_service.repository.UserRepository;
import com.example.book_ecommerce_api_service.service.CartService;
import com.example.book_ecommerce_api_service.type.BookStatus;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookItemRepository bookItemRepository;
    private final CartRepository cartRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public BookItemDto addCart(Long bookId, int amount, String token) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        if (book.getStatus().equals(BookStatus.SOLD_OUT)){
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        if (book.getStatus().equals(BookStatus.NOT_AVAILABLE)){
            throw new CustomException(ErrorCode.NOT_AVAILABLE);
        }

        String userEmail = jwtUtil.getEmail(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Cart> cartList = cartRepository.findByUserEmail(userEmail);

        BookItemDto bookItemDto = null;

        boolean check = false;

        for (Cart cart : cartList){
            if (cart.getBookItem().getBook().getName().equals(book.getName())){
                int totalAmount = cart.getBookItem().getAmount() + amount;

                if (totalAmount > book.getAmount()){
                    throw new CustomException(ErrorCode.OUT_OF_STOCK);
                }

                cart.getBookItem().setAmount(totalAmount);
                bookItemRepository.save(cart.getBookItem());
                cartRepository.save(cart);
                bookItemDto = BookItemDto.fromEntity(cart.getBookItem());
                check = true;
                break;
            }
        }

        if (!check){
            if (amount > book.getAmount()){
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }

            BookItem bookItem = BookItem.builder()
                    .user(user)
                    .book(book)
                    .amount(amount)
                    .build();

            bookItemRepository.save(bookItem);
            cartRepository.save(Cart.builder()
                    .user(user)
                    .bookItem(bookItem)
                    .build());

            bookItemDto = BookItemDto.fromEntity(bookItem);
        }

        return bookItemDto;
    }

    @Transactional
    public BookItemDto updateCart(Long cartId, int amount, String token){
        String userEmail = jwtUtil.getEmail(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART_ID));

        if (!cart.getUser().getEmail().equals(user.getEmail())){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        if (cart.getBookItem().getBook().getAmount() < amount){
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        if (cart.getBookItem().getBook().getStatus() == BookStatus.NOT_AVAILABLE){
            throw new CustomException(ErrorCode.NOT_AVAILABLE);
        }

        cart.getBookItem().setAmount(amount);
        bookItemRepository.save(cart.getBookItem());
        cartRepository.save(cart);

        return BookItemDto.fromEntity(cart.getBookItem());
    }

    @Transactional
    public void deleteCart(Long cartId, String token){
        String userEmail = jwtUtil.getEmail(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CART_ID));

        if (!cart.getUser().getEmail().equals(user.getEmail())){
            throw new CustomException(ErrorCode.NO_AUTH);
        }

        bookItemRepository.delete(cart.getBookItem());
        cartRepository.delete(cart);
    }

    public CartDto searchCart(String token) {
        String userEmail = jwtUtil.getEmail(token);

        List<Cart> cartList = cartRepository.findByUserEmail(userEmail);

        List<BookItemDto> bookItemDtoList = new ArrayList<>();

        int totalPrice = 0;
        int totalAmount = 0;

        for (Cart cart : cartList){
            bookItemDtoList.add(BookItemDto.fromEntity(cart.getBookItem()));
            totalPrice += cart.getBookItem().getAmount() * cart.getBookItem().getBook().getPrice();
            totalAmount += cart.getBookItem().getAmount();
        }

        return CartDto.builder()
                .userEmail(userEmail)
                .bookItemDtoList(bookItemDtoList)
                .totalPrice(totalPrice)
                .totalAmount(totalAmount)
                .build();
    }
}
