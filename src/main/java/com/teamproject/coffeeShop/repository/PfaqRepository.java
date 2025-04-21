package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.Pfaq;
import com.teamproject.coffeeShop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/* 상품문의 Repository - 진우 */
public interface PfaqRepository extends JpaRepository<Pfaq,Long> {
    
    // 특정 상품에 대한 상품문의 보기
    Page<Pfaq> findByCoffeeBean_Id(Long coffeeBeanId, Pageable pageable);

    // 특정 멤버가 작성한 상품문의 조회
    Page<Pfaq> findByMemberId(Long memberId, Pageable pageable);
}
