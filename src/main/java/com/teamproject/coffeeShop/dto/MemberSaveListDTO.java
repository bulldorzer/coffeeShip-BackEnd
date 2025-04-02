package com.teamproject.coffeeShop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 관심상품 리스트 DTO - 이재민
@Data
@NoArgsConstructor
@Builder
public class MemberSaveListDTO {
    private Long memberSaveId;

    private Long coffeeBeanId;
    private String name;
    private int price;

    // 상품 썸네일 이미지
    private String imageFile;

    public MemberSaveListDTO(Long memberSaveId, Long coffeeBeanId,
                             String name, int price, String imageFile) {
        this.memberSaveId = memberSaveId;
        this.coffeeBeanId = coffeeBeanId;
        this.name = name;
        this.price = price;
        this.imageFile = imageFile;
    }
}
