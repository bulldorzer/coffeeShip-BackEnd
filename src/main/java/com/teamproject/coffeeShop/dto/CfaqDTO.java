package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CfaqDTO {

    // 상품후기 게시판 아이디
    private Long cfaqId;

    // 상품후기 제목
    private String title;

    // 상품후기 내용
    private String writer;

    // 글쓴이
    private String content;

    // 글쓴날짜
    private LocalDate postDate;

    // 응답상태
    private Answer answer;
}
