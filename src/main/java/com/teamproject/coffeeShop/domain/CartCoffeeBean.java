package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"cart", "coffeeBean"} )
/* 장바구니 - 원두 연결(조인) 테이블 : 나영일 */
public class CartCoffeeBean {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coffee_bean_id") // 필드 이름 변경됨
    private CoffeeBean coffeeBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id") // 필드 이름 변경됨
    private Cart cart;

    private int qty;

    public void changeQty(int qty){
        this.qty = qty;
    }

}
