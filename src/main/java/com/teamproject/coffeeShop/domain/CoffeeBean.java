package com.teamproject.coffeeShop.domain;

import com.teamproject.coffeeShop.exception.NotEnoughStockException;
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
    private String country; // 원두 원산지 (from은 예약어로 사용 불가 -> country로 대체)
    private String amount;  // 원두 양(g)

    private int stockQty;  // 원두 재고량

    @Enumerated(EnumType.STRING)
    private CoffeeBeanTaste taste;  // 원두 맛 (enum)

    private boolean delFlag;    // 화면 표시 여부
    private boolean eventFlag;  // 이벤트 상품 여부
    private boolean grindFlag;  // 원두 분쇄 여부

    @ElementCollection
    @Builder.Default
    private List<CoffeeBeanImage> imageList = new ArrayList<>();    // 원두 이미지(사진) 리스트

    // 재고 증가
    public void addStock(int qty) {
        this.stockQty += qty;
    }

    // 재고 감소
    public void removeStock(int qty) {
        int restStock = this.stockQty - qty;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQty = restStock;
    }

    // imageList에 이미지 추가
    public void addImage(CoffeeBeanImage image) {
        image.setOrd(this.imageList.size());
        imageList.add(image);
    }

    // 이미지 파일명을 입력받아 이미지를 추가
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

    public void changeCountry(String country) {
        this.country = country;
    }

    public void changeAmount(String amount) {
        this.amount = amount;
    }

    public void changeStockQuantity(int stockQty) {
        this.stockQty = stockQty;
    }

    public void changeTaste(CoffeeBeanTaste taste) {
        this.taste = taste;
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