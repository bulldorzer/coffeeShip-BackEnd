package com.teamproject.coffeeShop.repository;

// 관심상품Repository - 이재민
import com.teamproject.coffeeShop.domain.Cart;
import com.teamproject.coffeeShop.domain.MemberSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface MemberSaveRepository extends JpaRepository<MemberSave, Long> {
    // memberId로 해당 사용자의 관심상품 알아내는 기능
    @Query("SELECT ms FROM MemberSave ms WHERE ms.member.id = :memberId")
    public Optional<MemberSave> getMemberSave(@Param("memberId") Long id);

    // 회원의 memberId로 관심상품을 조회
    @Query("select ms from MemberSave ms where ms.member.id = :memberId")
    Optional<MemberSave> findByMemberId(@Param("memberId") Long memberId);
}
