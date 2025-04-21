package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/* 상품문의 DTO - 진우 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PfaqDTO {

    // 상품문의 게시판 아이디
    private Long id;

    // 후기 상품명
    private String productName;

    // 상품문의 제목
    private String title;

    // 상품문의 내용
    private String writer;

    // 글쓴이
    private String content;

    // 글쓴날짜
    private LocalDate postDate;

    // 응답상태
    private String answer;
}
