package com.example.book_ecommerce_api_service.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_VALID_PATTERN("비밀번호는 최소 영어 1자리, 숫자 1자리, 특수문자 1자리 이상을 포함해야 합니다."),
    NOT_FOUND_BOOK("해당하는 책을 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY("해당하는 카테고리를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL("해당하는 이메일을 찾을 수 없습니다."),
    NOT_FOUND_USER("해당하는 유저를 찾을 수 없습니다."),
    NOT_FOUND_ORDER("주문 정보가 존재하지 않습니다."),
    NOT_FOUND_CART_ID("해당 장바구니 정보를 찾을 수 없습니다."),
    NOT_FOUND_CART("장바구니가 비어 있습니다."),
    NOT_FOUND_REVIEW("해당하는 리뷰를 찾을 수 없습니다."),
    NOT_AVAILABLE("구입 불가 상태의 책입니다."),
    NOT_LOGIN("로그인이 필요합니다."),
    OUT_OF_STOCK("재고가 없습니다."),
    ALREADY_EXISTS_BOOK("이미 존재하는 이름의 책입니다."),
    ALREADY_EXISTS_EMAIL("이미 존재하는 계정입니다."),
    UN_MATCH_CONFIRM_PASSWORD("비밀번호 확인이 일치하지 않습니다."),
    NO_AUTH("권한이 없습니다.");

    private final String description;
}
