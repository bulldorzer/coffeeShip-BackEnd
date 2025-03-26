package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.Answer;
import com.teamproject.coffeeShop.domain.CoffeeBean;
import com.teamproject.coffeeShop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/* 상품후기 게시판 DTO - 진우 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    // 상품후기 게시판 아이디
    private Long reviewId;

    // 상품후기 제목
    private String title;

    // 상품후기 내용
    private String writer;

    // 글쓴이
    private String content;

    // 글쓴날짜
    private LocalDate postDate;

}
