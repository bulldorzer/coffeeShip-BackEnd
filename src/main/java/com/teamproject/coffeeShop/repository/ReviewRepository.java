package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/* Review Repository - 진우 */
public interface ReviewRepository extends JpaRepository<Review,Long> {

    // 특정 상품에 대한 리뷰 보기
    Page<Review> findByCoffeeBean_Id(Long coffeeBeanId, Pageable pageable);

    // 특정 멤버가 작성한 리뷰 리스트 조회
    Page<Review> findByMemberId(Long memberId, Pageable pageable);

}
