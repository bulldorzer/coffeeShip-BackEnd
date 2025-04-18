package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Cfaq;
import com.teamproject.coffeeShop.domain.Order;
import com.teamproject.coffeeShop.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/* 고객문의게시판 Repository - 진우 */
public interface CfaqRepository extends JpaRepository<Cfaq,Long> {

    // 특정 멤버가 작성한 리뷰 리스트 조회
    Page<Cfaq> findByMemberId(Long memberId, Pageable pageable);
}
