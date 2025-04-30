package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.OrderStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/* 주문서DTO - 진우 */
@Data
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

    // 커피 원두 상품 DTO
    private  OrderItemRequestDTO orderItemRequestDTO;

    // 배달DTO
    private DeliveryDTO deliveryDTO;

    // 도로명주소
    private String city;
    private String street;
    private String zipcode;

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
