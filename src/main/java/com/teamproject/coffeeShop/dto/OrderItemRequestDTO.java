package com.teamproject.coffeeShop.dto;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private  Long coffeeBeanId; // 커피 원두 상품 아이디
    private int orderPrice; // 주문 가격
    private int qty; // 주문 수량
}
