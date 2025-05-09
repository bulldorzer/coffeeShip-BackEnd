package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.ReviewDTO;
import org.springframework.data.domain.Pageable;


// 상품 후기게시판 - 진우
public interface ReviewService {

    // 상품 후기글 등록
    public Long register(Long memberId, Long coffeeBeanId, ReviewDTO reviewDTO);

    // 상품 후기글 상세보기
    public ReviewDTO getReview (Long reviewId);

    // 상품 후기글 수정
    public void modify(ReviewDTO reviewDTO);

    // 상품 후기글 삭제
    public void remove(Long reviewId);

    // 전체 상품 후기 목록 조회(페이징 포함)
    public CustomPage<ReviewDTO> getAllReviews(Pageable pageable);

    // 특정 상품에 대한 리뷰 보기
    public CustomPage<ReviewDTO> getReviewsByCoffeeBeanId(Long coffeeBeanId, Pageable pageable);

    // 특정 멤버가 작성한 리뷰 리스트 조회
    public CustomPage<ReviewDTO> getReviewsByMemberId(Long memberId, Pageable pageable);
}
