package com.teamproject.coffeeShop.domain;


import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
/* 원두 이미지 테이블 - 나영일 */
public class CoffeeBeanImage {

    private String fileName;    // 이미지 파일명

    private int ord;            // 이미지 순서

    public void setOrd(int ord){
        this.ord = ord;
    }
}