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
    private Long orderId;
    private Long coffeeBeanId;
    private LocalDate orderDate;
    private String status;
    private String coffeeName;
    private int totalPrice;
    private int qty;

}
