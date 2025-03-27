package com.teamproject.coffeeShop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "member")
/* 장바구니 엔티티 - 나영일 */
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 장바구니 고유번호

    // private Member member;   // member_id 와 매핑 (FK, 회원 고유번호)
}
