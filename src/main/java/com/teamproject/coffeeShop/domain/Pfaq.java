package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/* 상품문의게시판 - 진우 */
@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pfaq {

    // PK 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pfaq_id")
    private Long id;

    // FK 설정 coffeeBean_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coffeeBean_id")
    private CoffeeBean coffeeBean;

    // FK 설정 member_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    // 게시글 제목
    private String title;

    // 내용 1500자 제한
    @Column(length = 6000)
    private String content;

    // 문의등록날짜
    private LocalDate postDate;

    // 답변
    // CHECK (확인중) RESPONSE (답변완료)
    @Enumerated(EnumType.STRING)
    private Answer answer;
}
