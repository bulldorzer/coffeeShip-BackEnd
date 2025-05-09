package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.CoffeeBeanTaste;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
/* 장바구니 원두 리스트 DTO - 나영일 */
public class CartCoffeeBeanListDTO {

    private Long cartCoffeeBeanId;  // 장바구니 원두 고유번호
    private int qty;                // 장바구니 원두 수량

    private Long coffeeBeanId;      // 원두 고유번호
    private String name;            // 원두 이름
    private int price;              // 원두 가격
    private String country;         // 원두 원산지
    private String amount;          // 원두 양(g)
    private CoffeeBeanTaste taste;  // 원두 맛

    private String imageFile;       // 이미지 파일?

    // 생성자
    public CartCoffeeBeanListDTO(Long cartCoffeeBeanId, int qty, Long coffeeBeanId, String name, int price,
                                 String country, String amount, CoffeeBeanTaste taste, String imageFile) {
        this.cartCoffeeBeanId = cartCoffeeBeanId;
        this.qty = qty;
        this.coffeeBeanId = coffeeBeanId;
        this.name = name;
        this.price = price;
        this.country = country;
        this.amount = amount;
        this.taste = taste;
        this.imageFile = imageFile;
    }

}
