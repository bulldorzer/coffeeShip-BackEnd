package com.teamproject.coffeeShop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String country;    // 원두 원산지
    private String amount;  // 원두 양(g)

    @JsonProperty("stockQty")
    private int stockQty;  // 원두 재고량

    private CoffeeBeanTaste taste;  // 원두 맛 (enum)

    private boolean delFlag;    // 화면 표시 여부
    private boolean eventFlag;  // 이벤트 상품 여부
    private boolean grindFlag;  // 원두 분쇄 여부

    private List<Long> categoryIds;     // 연동할 카테고리 고유번호 목록(추가)

    @JsonIgnore
    @Builder.Default
    // 서버에 보내지는 실제 파일 데이터의 리스트 - 사용자가 새로운 파일을 업로드할 때 사용
    private List<MultipartFile> files = new ArrayList<>();

    @Builder.Default
    // 업로드된 파일의 이름만 문자열로 보관하는 리스트 - 데이터베이스에 파일 이름들을 처리하는 용도로 사용
    private List<String> uploadFileNames = new ArrayList<>();

    // uploadFileNames 가져오기
    public List<String> getUploadFileNames() {
        return (uploadFileNames == null || uploadFileNames.isEmpty())
                ? List.of("default.png")  // 자동으로 기본 이미지 반환
                : uploadFileNames;
    }
}
/*
     // DTO 생성자 추후 추가 가능 (JPQL 매핑과 일치하도록 변경) - 아래 예시
     public CoffeeBeanDTO(Long id, String name, int price, String from, String amount, List<String> uploadFileNames) {
         this.id = id;
         this.name = name;
         this.price = price;
         this.country = country;
         this.amount = amount;
         this.uploadFileNames = (uploadFileNames != null) ? uploadFileNames : new ArrayList<>(); // Null 방지
     }
*/