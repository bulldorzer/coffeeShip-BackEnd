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
public class CartCoffeeBeanDTO {
    private Long id;            // 장바구니 원두 고유번호
    private Long cartId;        // 장바구니 고유번호
    private Long coffeeBeanId;  // 원두 고유번호
    private int qty;    // 원두 수량
}
