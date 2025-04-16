package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/* 주문서DTO - 진우 */
@Getter
@NoArgsConstructor
public class OrderDTO {

    // 주문서 아이디
    private Long orderId;

    // 주문서 이름
    private String name;

    // 주문서 생성날짜
    private LocalDate orderDate;

    // 주문서 상태
    private OrderStatus status;

    // 도로명주소
    private String city;
    private String street;
    private String zipcode;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    // ✅ Setter 추가
    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    // DTO 초기화 생성자
    public OrderDTO(
            Long orderId, String name, LocalDate orderDate, OrderStatus status,
            String city, String street, String zipcode) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.status = status;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
