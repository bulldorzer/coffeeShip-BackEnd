package com.teamproject.coffeeShop.domain;

/* 주문서 상태 Enum - 진우 */
public enum OrderStatus {
    PENDING,        // 주문대기
    ORDER,          // 주문
    CANCEL,         // 주문취소
    COMP,       // 배송중
    COMPLETE        // 배달완료
}
