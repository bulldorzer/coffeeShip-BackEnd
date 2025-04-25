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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coffeeBean_id")
    private CoffeeBean coffeeBean;

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

    // 리뷰 보유 여부 true : 리뷰 보유, false : 리뷰 미보유
    private boolean hasReview;

    // 주문상품 생성
    public static OrderCoffeeBean createOrderItem(Order order, CoffeeBean coffeeBean, int orderPrice, int qty){

        OrderCoffeeBean orderCoffeeBean = new OrderCoffeeBean();

        orderCoffeeBean.setOrder(order); // 주문서 id가 저장됨 (order_id)
        orderCoffeeBean.setCoffeeBean(coffeeBean); // 상품id (item_id)
        orderCoffeeBean.setOrderPrice(orderPrice); // 상품가격
        orderCoffeeBean.setQty(qty); // 주문수량
        orderCoffeeBean.setName(coffeeBean.getName()); // 원두상품이름
        orderCoffeeBean.setOrderDate(order.getOrderDate()); // 주문서등록날짜

        coffeeBean.removeStock(qty); // 재고 감소
        return orderCoffeeBean;
    }

    // 주문 취소 - 재고 추가
    public void cancel(){
        getCoffeeBean().addStock(qty);
    }

    // 주문상품 금액 계산 - 상품당 단가 * 수량
    public int getTotalPrice(){
        return getOrderPrice() * getQty();
    }

    // 리뷰 보유 여부 변경
    public void changeHasReview(boolean hasReview) {
        this.hasReview = hasReview;
    }



}
