package com.example.book_ecommerce_api_service.service.impl;

import com.example.book_ecommerce_api_service.domain.*;
import com.example.book_ecommerce_api_service.dto.BookItemDto;
import com.example.book_ecommerce_api_service.dto.CartDto;
import com.example.book_ecommerce_api_service.dto.UserOrderDto;
import com.example.book_ecommerce_api_service.exception.CustomException;
import com.example.book_ecommerce_api_service.jwt.JWTUtil;
import com.example.book_ecommerce_api_service.repository.*;
import com.example.book_ecommerce_api_service.service.UserOrderService;
import com.example.book_ecommerce_api_service.type.BookStatus;
import com.example.book_ecommerce_api_service.type.ErrorCode;
import com.example.book_ecommerce_api_service.type.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserOrderServiceImpl implements UserOrderService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final UserOrderRepository userOrderRepository;
    private final JWTUtil jwtUtil;
    private final BookRepository bookRepository;
    private final BookItemRepository bookItemRepository;

    @Transactional
    public UserOrderDto completeOrder(String token) {
        String userEmail = jwtUtil.getEmail(token);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<Cart> cartList = cartRepository.findByUserEmail(userEmail);

        if (cartList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_CART);
        }

        List<BookItem> bookItemList = new ArrayList<>();

        Optional<UserOrder> userOrder = userOrderRepository.findFirstByOrderByOrderIdDesc();

        long lastOrderId = 0;

        if (userOrder.isPresent()) {
            lastOrderId = userOrder.get().getOrderId();
        }

        for (Cart cart : cartList) {
            BookItem bookItem = cart.getBookItem();

            if (bookItem.getAmount() > bookItem.getBook().getAmount()){
                throw new CustomException(ErrorCode.OUT_OF_STOCK);
            }

            if (bookItem.getAmount().equals(bookItem.getBook().getAmount())){
                bookItem.getBook().setStatus(BookStatus.SOLD_OUT);
            }

            bookItem.getBook().setAmount(bookItem.getBook().getAmount() - bookItem.getAmount());

            userOrderRepository.save(UserOrder.builder()
                    .orderId(lastOrderId + 1)
                    .user(user)
                    .bookItem(bookItem)
                    .status(OrderStatus.ORDER_COMPLETE)
                    .orderDateTime(LocalDateTime.now())
                    .build());

            bookItemList.add(bookItem);

            cartRepository.delete(cart);
        }

        return UserOrderDto.builder()
                .orderId(lastOrderId + 1)
                .userEmail(userEmail)
                .bookItemList(bookItemList.stream().map(BookItemDto::fromEntity).toList())
                .orderStatus(OrderStatus.ORDER_COMPLETE)
                .orderDateTime(LocalDateTime.now())
                .build();
    }

    @Transactional
    public void cancelOrder(Long orderId, String token){
        String userEmail = jwtUtil.getEmail(token);

        List<UserOrder> userOrderList = userOrderRepository.findByOrderId(orderId);

        if (userOrderList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_ORDER);
        }

        for (UserOrder userOrder : userOrderList){
            if (!userOrder.getUser().getEmail().equals(userEmail)){
                throw new CustomException(ErrorCode.NO_AUTH);
            }

            Book book = userOrder.getBookItem().getBook();
            book.setAmount(book.getAmount() + userOrder.getBookItem().getAmount());

            if (book.getStatus() == BookStatus.SOLD_OUT){
                book.setStatus(BookStatus.AVAILABLE);
            }

            bookRepository.save(book);
            bookItemRepository.delete(userOrder.getBookItem());
            userOrderRepository.delete(userOrder);
        }
    }

    public List<UserOrderDto> searchOrder(String token){
        String userEmail = jwtUtil.getEmail(token);

        List<UserOrder> userOrderList = userOrderRepository.findByUser_Email(userEmail);

        if (userOrderList.isEmpty()){
            throw new CustomException(ErrorCode.NOT_FOUND_ORDER);
        }

        HashMap<Long, List<UserOrder>> userOrderMap = new HashMap<>();

        for (UserOrder userOrder : userOrderList){
            if (userOrderMap.containsKey(userOrder.getOrderId())){
                userOrderMap.get(userOrder.getOrderId()).add(userOrder);
            } else {
                userOrderMap.put(userOrder.getOrderId(), new ArrayList<>(Arrays.asList(userOrder)));
            }
        }

        List<UserOrderDto> userOrderDtoList = new ArrayList<>();

        for (Map.Entry<Long, List<UserOrder>> entry : userOrderMap.entrySet()){
            userOrderDtoList.add(UserOrderDto.builder()
                    .orderId(entry.getKey())
                    .userEmail(userEmail)
                    .bookItemList(entry.getValue().stream().map(UserOrder::getBookItem).map(BookItemDto::fromEntity).toList())
                    .orderStatus(entry.getValue().get(0).getStatus())
                    .orderDateTime(entry.getValue().get(0).getOrderDateTime())
                    .build());
        }

        return userOrderDtoList;
    }
}
