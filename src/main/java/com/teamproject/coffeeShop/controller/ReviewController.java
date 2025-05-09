package com.teamproject.coffeeShop.controller;


import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.ReviewDTO;
import com.teamproject.coffeeShop.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰글등록
    @PostMapping("/{memberId}/{coffeeBeanId}")
    public ResponseEntity<Map<String, Long>> register(
            @PathVariable Long memberId,
            @PathVariable Long coffeeBeanId,
            @RequestBody ReviewDTO reviewDTO){

        log.info("MemberId: " + memberId+ " || CoffeeBeanId: " + coffeeBeanId+ " || ReviewDTO: " + reviewDTO);

        Long reviewId = reviewService.register(memberId,coffeeBeanId,reviewDTO);

        return ResponseEntity.ok(Map.of("RESULT", reviewId));
    }

    // 리뷰 상세보기
    @GetMapping("/list/{reviewId}")
    public ResponseEntity<ReviewDTO> get(@PathVariable(name ="reviewId") Long reviewId) {

        return ResponseEntity.ok(reviewService.getReview(reviewId));
    }

    // 리뷰 전체목록 보기 - 페이징 처리
    @GetMapping("/list")
    public ResponseEntity<CustomPage<ReviewDTO>>
    list(@PageableDefault(page = 0, size = 6)Pageable pageable) {

        return ResponseEntity.ok(reviewService.getAllReviews(pageable));
    }

    // 특정 상품에 대한 리뷰 보기
    @GetMapping("/list/product/{coffeeBeanId}")
    public ResponseEntity<CustomPage<ReviewDTO>> getReviewsByCoffeeBean(
            @PathVariable Long coffeeBeanId,
            @PageableDefault(page = 0, size = 6) Pageable pageable) {

        return ResponseEntity.ok(reviewService.getReviewsByCoffeeBeanId(coffeeBeanId, pageable));
    }

    // 특정 멤버가 작성한 리뷰 리스트 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<CustomPage<ReviewDTO>> getReviewsByMember(
            @PathVariable Long memberId,
            @PageableDefault(size = 6) Pageable pageable) {

        return ResponseEntity.ok(reviewService.getReviewsByMemberId(memberId, pageable));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Map<String, String>> modify(
            @PathVariable(name="reviewId") Long reviewId,
            @RequestBody ReviewDTO reviewDTO) {

        reviewDTO.setId(reviewId);

        log.info("Modify: " + reviewDTO);

        reviewService.modify(reviewDTO);

        return ResponseEntity.ok(Map.of("RESULT", "SUCCESS"));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Map<String, String>> remove(
            @PathVariable(name="reviewId") Long reviewId){

        log.info("Remove:  " + reviewId);

        reviewService.remove(reviewId);

        return ResponseEntity.ok(Map.of("RESULT", "SUCCESS"));
    }
}
