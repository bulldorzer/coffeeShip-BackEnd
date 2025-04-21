package com.teamproject.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// 마이페이지 주문내역 조회 사용 DTO - 이재민
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {
    private Long orderId; //주문서아이디
    private Long coffeeBeanId; // 주문 커피원두상품 아이디
    private LocalDate orderDate; // 주문날짜
    private String status; // 주문상태
    private String coffeeName; // 주문 원두 상품명
    private int totalPrice; // 총 주문 가격
    private int qty; // 총 주문 수량

}
