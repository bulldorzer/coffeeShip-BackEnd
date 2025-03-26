package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"cart", "coffeeBean"} )
/* 장바구니, 원두 연결(조인) 테이블 : 나영일 */
public class CartCoffeeBean {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    // 장바구니 원두 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coffeebean_id") // 필드 이름 변경됨
    private CoffeeBean coffeeBean;       // 원두 참조번호(FK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id") // 필드 이름 변경됨
    private Cart cart;            // 장바구니 참조번호(FK)

    private int qty;              // 원두 수량

    public void changeQty(int qty){
        this.qty = qty;
    }

}
