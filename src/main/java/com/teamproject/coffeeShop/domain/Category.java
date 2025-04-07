package com.teamproject.coffeeShop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
/* 카테고리 엔티티 - 나영일 */
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;            // 카테고리 고유번호
    private String name;        // 카테고리 이름
    private boolean delFlag;    // 카테고리 삭제 여부 (Soft Delete)

    // 해당 카테고리에 소속될 상품들
    @OneToMany(mappedBy = "category")
    private List<CategoryCoffeeBean> categoryCoffeeBeans = new ArrayList<>();

    // 상위 카테고리 + 하위카테고리 : 양방향 관계 설정
    @JsonIgnore // 무한루프 끊어줌 => JSON 직렬화에서 parent 제외하고 무한루프 방지
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 하위 카테고리
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

}