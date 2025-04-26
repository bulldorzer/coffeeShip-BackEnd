package com.teamproject.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* 장바구니 - 원두 연결 DTO : 나영일 */
// 상품 상세 페이지에서 장바구니 추가했을 때 받아올 정보들
public class CartCoffeeBeanDTO {
    private Long cartCoffeeBeanId;      // 장바구니 원두 고유번호
    private Long coffeeBeanId;          // 원두 고유번호
    private Long cartId;                // 장바구니 고유 번호
    private boolean grind_flag;         // 프론트에서 받아올 분쇄여부
    private int qty;                    // 장바구니 원두 수량
}


