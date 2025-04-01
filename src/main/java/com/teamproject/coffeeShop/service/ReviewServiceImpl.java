package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Review;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.ReviewDTO;
import com.teamproject.coffeeShop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/* 상품후기 게시판 구현 - 진우 */
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ModelMapper modelMapper;
    private final ReviewRepository reviewRepository;

    // 리뷰 등록
    @Override
    public Long register(ReviewDTO reviewDTO) {
        log.info("Review Board register");

        // ReviewDTO를 Review 엔티티로 변환
        Review review = modelMapper.map(reviewDTO,Review.class);

        // Review 데이터 DB에 저장
        Review savedReview = reviewRepository.save(review);

        // Review 저장된 아이디 반환
        return savedReview.getId();
    }

    // 리뷰 상세보기
    @Override
    public ReviewDTO getReview(Long reviewId) {

        //엔티티 데이터 추출
        Optional<Review> result = reviewRepository.findById(reviewId);

        // 자료형 변경 Optional -> Review
        Review review = result.orElseThrow();

        // Review -> ReviewDTO로 자료변환
        ReviewDTO dto = modelMapper.map(review,ReviewDTO.class);

        return dto;
    }

    // 리뷰 내용 수정
    @Override
    public void modify(ReviewDTO reviewDTO) {

        // 수정할 리뷰 데이터 가져오기
        Optional<Review> result = reviewRepository.findById(reviewDTO.getId());

        // 자료형 변경 Optional -> Review
        Review review = result.orElseThrow();

        // 화면에서 받아온 수정값 받아오기
        review.changeTitle(reviewDTO.getTitle());
        review.changeScore(reviewDTO.getScore());
        review.changeContent(reviewDTO.getContent());
        review.changePostDate(reviewDTO.getPostDate());

        // 수정 데이터 저장
        reviewRepository.save(review);
    }

    // 리뷰 내용 삭제
    @Override
    public void remove(Long reviewId) {
        reviewRepository.deleteById(reviewId);

    }

    // 리뷰 목록 페이징 처리
    @Override
    public CustomPage<ReviewDTO> getAllReviews(Pageable pageable) {

        // Review엔티티 페이징 처리된 데이터 조회
        Page<Review> reviewPage = reviewRepository.findAll(pageable);

        // 조회된 페이지 없으면 예외처리
        if (reviewPage.isEmpty()) throw new IllegalArgumentException("조회된 데이터가 존재하지 않습니다.");

        // 엔티티 -> DTO 변환
        Page<ReviewDTO> dtoPage = reviewPage.map(review -> modelMapper.map(review,ReviewDTO.class));
        log.info("=======<ReviewDTO Page>=======");
        log.info(dtoPage.getContent());

        // DTO 페이지 네이션 정보 추가( 별도의 DTO 만들기)
        int groupSize = 10;
        return CustomPage.of(dtoPage,groupSize);
    }
}
