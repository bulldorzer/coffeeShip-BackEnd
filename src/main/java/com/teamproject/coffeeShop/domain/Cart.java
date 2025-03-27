package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 필드 이름 변경됨
    private Member member;   // member_id(PK) 와 매핑 (FK, 회원 고유번호)
}
