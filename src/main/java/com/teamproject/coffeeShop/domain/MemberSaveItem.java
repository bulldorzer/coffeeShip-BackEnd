package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// 관심상품 아이템 Repository - 이재민
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberSaveItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coffeeBean_id")
    private CoffeeBean coffeeBean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_save_id")
    private MemberSave memberSave;
}
