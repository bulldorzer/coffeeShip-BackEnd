package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
/* 카테고리 원두(상품) 연결 엔티티 - 나영일 */
public class CategoryCoffeeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_coffee_bean_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "coffee_bean_id")
    private CoffeeBean coffeeBean;

    public CategoryCoffeeBean(Category category, CoffeeBean coffeeBean) {
        this.category = category;
        this.coffeeBean = coffeeBean;
    }
}
