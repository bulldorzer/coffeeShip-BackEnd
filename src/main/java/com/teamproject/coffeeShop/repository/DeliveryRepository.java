package com.teamproject.coffeeShop.repository;

// 배송Repository - 이재민
import com.teamproject.coffeeShop.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // 특정 회원의 모든 배송정보를 불러오는 기능
    @Query("SELECT d FROM Delivery d WHERE d.member.id = :memberId")
    List<Delivery> findAllByMemberId(@Param("memberId") Long memberId);

    // 특정 회원의 특정 배송정보를 불러오는 기능
    @Query("SELECT d FROM Delivery d WHERE d.member.id = :memberId AND d.id = :deliveryId")
    Delivery findByMemberIdAndDeliveryId(
            @Param("memberId") Long memberId, @Param("deliveryId") Long deliveryId);
}
