package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Cart;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
/* 장바구니 서비스 인터페이스 - 나영일 */
public interface CartService {

    // 장바구니 원두 추가
    void addCart(String email, Long coffeeBeanId, int qty, boolean grindFlag);

    // 모든 장바구니 원두 목록 조회
    List<CartCoffeeBeanListDTO> getCartCoffeeBeans(String email);

    // 회원의 이메일로 CartId 가져오기
    Long getCart(String email);

    // 장바구니 원두 삭제
    void removeOne(Long cartId, Long cartCoffeeBeanId);

    // 특정 회원 장바구니 상품 전체 삭제
    void removeAll(Long cartId);

    // 옵션 ( 수량, 분쇄여부 ) 변경 기능
    void changeOption(CartCoffeeBeanListDTO cartCoffeeBeanDTO);
}
