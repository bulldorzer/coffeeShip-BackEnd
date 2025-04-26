package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.CoffeeBeanTaste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
/* 장바구니 원두 리스트 DTO - 나영일 */
public class CartCoffeeBeanListDTO {

    // 장바구니 화면에서 조회할 때 필요한 값들
    private Long cartCoffeeBeanId;  // 장바구니 원두 고유번호
    private Long cartId;            // 장바구니 고유번호
    private Long memberId;          // 회원 아이디
    private Long coffeeBeanId;      // 원두 고유번호
    private String name;            // 원두 이름
    private int price;              // 원두 가격
    private int qty;                // 장바구니 원두 수량
    private boolean grind_flag;     // 분좨여부
    private String memberShip;

    private String imageFile;       // 이미지 파일?

    // 생성자
    public CartCoffeeBeanListDTO(Long cartCoffeeBeanId, Long cartId, Long memberId, Long coffeeBeanId, String name, int price, int qty, boolean grind_flag, String memberShip, String imageFile) {
        this.cartCoffeeBeanId = cartCoffeeBeanId;
        this.cartId = cartId;
        this.memberId = memberId;
        this.coffeeBeanId = coffeeBeanId;
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.grind_flag = grind_flag;
        this.memberShip = memberShip;

        this.imageFile = imageFile;
    }
}



