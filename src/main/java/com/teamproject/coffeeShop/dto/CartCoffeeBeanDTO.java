package com.teamproject.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* 장바구니 - 원두 연결 DTO : 나영일 */
public class CartCoffeeBeanDTO {
    private Long id;            // 장바구니 원두 고유번호
    private Long cartId;        // 장바구니 고유번호
    private Long coffeeBeanId;  // 원두 고유번호
    private int qty;    // 원두 수량
}

/*
     // 인덱스 추후 추가 가능
     @Table(
            name="tbl_cart_item",
            indexes = {
            @Index(name = "idx_cartitem_cart", columnList = "cart_cno"),
            @Index(name = "idx_cartitem_pno_cart", columnList = "product_pno, cart_cno")
     })

     // 설명
     @Index(name = "idx_cartitem_cart", columnList = "cart_cno"),
     - 인덱스 이름 : idx_cartitem_cart
     - 컬럼 이름 : cart_cno ---에 대해서 단일컬럼 인덱스를 생성

     @Index(name = "idx_cartitem_pno_cart", columnList = "product_pno, cart_cno")
     - 인덱스 이름 :  idx_cartitem_pno_cart
     - 복합인덱스 : product_pno, cart_cno
         - 2개 이상의 컬럼을 결합하여 하나의 인덱스로 지정하는 방식
                 --> 조회 속도 향상 시킴, 최적화, 불필요한 인덱스 줄이기

        ex)  where product_pno = ? and cart_cno =?
*/

