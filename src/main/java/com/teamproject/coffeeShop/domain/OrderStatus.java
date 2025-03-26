package com.teamproject.coffeeShop.domain;

/* 주문서 상태 Enum - 진우 */
public enum OrderStatus {
    PENDING,        // 주문대기
    ORDER,          // 주문
    CANCEL,         // 주문
    DELIVERY,       // 배송중
    DELIVERY_OVER   // 배달완료
}
