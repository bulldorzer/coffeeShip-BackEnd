package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
/* 장바구니 서비스 인터페이스 - 나영일 */
public interface CartService {

    // 장바구니 원두 추가 또는 변경(수정)
    List<CartCoffeeBeanListDTO> addOrModify(CartCoffeeBeanDTO cartCoffeeBeanDTO);

    // 모든 장바구니 원두 목록 조회
    List<CartCoffeeBeanListDTO> getCartCoffeeBeans(String email);

    // 장바구니 원두 삭제
    List<CartCoffeeBeanListDTO> remove(Long cartCoffeeBeanId);

}
