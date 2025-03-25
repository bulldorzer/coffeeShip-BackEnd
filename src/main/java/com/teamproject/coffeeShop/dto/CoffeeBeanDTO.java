package com.teamproject.coffeeShop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamproject.coffeeShop.domain.CoffeeBeanTaste;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/* 원두 DTO - 나영일 */
public class CoffeeBeanDTO {

    private Long id;        // 원두 고유번호

    private String name;    // 원두 이름
    private int price;      // 원두 가격
    private String from;    // 원두 원산지
    private String amount;  // 원두 양(g)

    private int stockQuantity;  // 원두 재고량

    private boolean delFlag;    // 화면 표시 여부
    private boolean eventFlag;  // 이벤트 상품 여부
    private boolean grindFlag;  // 원두 분쇄 여부

    private CoffeeBeanTaste taste;  // 원두 맛 (enum)

    @JsonIgnore
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();      // 이미지 파일 리스트

    @Builder.Default
    private List<String> uploadFileNames = new ArrayList<>();   // 이미지 파일명 리스트

    // uploadFileNames 가져오기
    public List<String> getUploadFileNames() {
        return (uploadFileNames == null || uploadFileNames.isEmpty())
                ? List.of("default.png")  // 자동으로 기본 이미지 반환
                : uploadFileNames;
    }
}
