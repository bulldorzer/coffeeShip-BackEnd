package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@ToString(exclude = "imageList")
/* 원두 엔티티 - 나영일 */
public class CoffeeBean {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 원두 고유번호

    private String name;    // 원두 이름
    private int price;      // 원두 가격
    private String from;    // 원두 원산지
    private String amount;  // 원두 양(g)

    private int stockQuantity;  // 원두 재고량

    private boolean delFlag;    // 화면 표시 여부
    private boolean eventFlag;  // 이벤트 상품 여부
    private boolean grindFlag;  // 원두 분쇄 여부

    @Enumerated(EnumType.STRING)
    private CoffeeBeanTaste taste;  // 원두 맛 (enum)

    @ElementCollection
    @Builder.Default
    private List<CoffeeBeanImage> imageList = new ArrayList<>();    // 원두 이미지(사진) 리스트

    // 재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 재고 감소 (재고 < 0 : 재고가 부족합니다.)
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new RuntimeException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    // imageList에 이미지 추가
    public void addImage(CoffeeBeanImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    // 이미지 파일명을 입력받아 추가
    public void addImageString(String fileName){
        CoffeeBeanImage coffeeBeanImage = CoffeeBeanImage.builder()
                .fileName(fileName)
                .build();
        addImage(coffeeBeanImage);
    }

    // imageList를 비운다.(전체 삭제)
    public void clearList() {
        this.imageList.clear();
    }

    // 변경 메서드
    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeAmount(String amount) {
        this.amount = amount;
    }

    public void changeTaste(CoffeeBeanTaste taste) {
        this.taste = taste;
    }

    public void changeStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void changeDelFlag(boolean delFlag) {
        this.delFlag = delFlag;
    }

    public void changeEventFlag(boolean eventFlag) {
        this.eventFlag = eventFlag;
    }

    public void changeGrindFlag(boolean grindFlag) {
        this.grindFlag = grindFlag;
    }
}