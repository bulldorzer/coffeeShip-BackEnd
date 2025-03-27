package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CartCoffeeBeanListDTO;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
/* 장바구니 서비스 인터페이스 - 나영일 */
public interface CartService {

    // 장바구니 상품 추가 변경
    public List<CartCoffeeBeanListDTO> addOrModify(CartCoffeeBeanDTO cartCoffeeBeanDTO);

    // 모든 장바구니 상품 목록 조회
    public List<CartCoffeeBeanListDTO> getCartItems(String email);

    // 장바구니 상품 삭제
    public List<CartCoffeeBeanListDTO> remove(Long id);

}
