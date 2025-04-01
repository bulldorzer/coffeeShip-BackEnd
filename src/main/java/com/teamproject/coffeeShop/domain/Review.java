package com.teamproject.coffeeShop.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/* 후기게시판 엔티티 - 진우 */
@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    // PK 설정
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    // FK coffeeBean_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coffeeBean_id")
    private CoffeeBean coffeeBean;

    // FK member_id 참조
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private Member member;

    // 후기점수
    private int score;

    // 게시판 제목
    private String title;

    // 게시판 글쓴이
    private String writer;

    // 게시판 내용글 1500자 한정
    @Column(length = 6000)
    private String content;

    // 게시판 등록날짜
    private LocalDate postDate;

    // 점수수정
    public void changeScore(int score) { this.score = score; }

    // 제목수정
    public void changeTitle(String title){
        this.title = title;
    }

    // 내용수정
    public void changeContent(String content){
        this.content = content;
    }
    
    // 날짜수정
    public void changePostDate(LocalDate postDate){
        this.postDate = postDate;
    }

}
