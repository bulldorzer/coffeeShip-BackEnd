package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/* 고객문의게시판 - 진우 */
@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cfaq {

    // PK 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cfaq_id")
    private Long id;

    // FK member_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    // 글 제목
    private String title;

    // 글쓴이
    private String writer;

    // 내용 1500자 제한
    @Column(length = 6000)
    private String Content;

    // 문의 등록날짜
    private LocalTime postDate;

    // 답변
    // CHECK (확인중) RESPONSE (답변완료)
    @Enumerated(EnumType.STRING)
    private Answer answer;
}
