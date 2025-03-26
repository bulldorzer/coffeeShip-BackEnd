package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


/* 주문서와원두 연결테이블 엔티티 - 진우 */
@Entity
@Getter
@Setter
public class OrderCoffeeBean {

    // PK 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_coffeebean_id") // db 관례적으로 작명을 snake style 사용
    private Long id;

    // FK coffeeBean_id 참조
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="item_id")
//    private Item item;

    // FK order_id 참조
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name="order_id")
    private Order order;

    // 검색어
    private String name;

    // 검색 날짜
    private LocalDate orderDate;

    // 주문가격
    private int orderPrice;

    // 주문수량
    private int qty;

    // 주문상품 생성
//    public static OrderItem createOrderItem(Order order, Item item, int orderPrice, int qty){
//
//        OrderItem orderItem = new OrderItem();
//
//        orderItem.setOrder(order); // 주문서 id가 저장됨 (order_id)
//        orderItem.setItem(item); // 상품id (item_id)
//        orderItem.setOrderPrice(orderPrice); // 상품가격
//        orderItem.setQty(qty); // 주문수량
//
//        item.removeStock(qty); // 재고 감소
//        return orderItem;
//    }

    // 주문 취소 - 재고 추가
//    public void cancel(){
//        getItem().addStock(qty);
//    }

    // 주문상품 금액 계산 - 상품당 단가 * 수량
    public int getTotalPrice(){
        return getOrderPrice() * getQty();
    }



}
