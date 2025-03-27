package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/* 주문서 엔티티 - 진우 */
@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    // PK 주문서아이디
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // FK member_id 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

     // FK delivery_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // order의 배달주소, 상태가 바뀌면 delivery의 데이터 배달주소나 상태가 동시에 바뀜
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    // 주문날짜
    private LocalDate orderDate;

    // 주문상태 (Enum)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 주문취소시 상태 변경
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소 불가");
        }
        this.status = OrderStatus.CANCEL;
    }
}
