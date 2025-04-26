package com.teamproject.coffeeShop.repository;

// 관심상품Repository - 이재민
import com.teamproject.coffeeShop.domain.Cart;
import com.teamproject.coffeeShop.domain.MemberSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;


public interface MemberSaveRepository extends JpaRepository<MemberSave, Long> {
    // memberId로 해당 사용자의 관심상품 알아내는 기능
    @Query("SELECT ms FROM MemberSave ms WHERE ms.member.id = :memberId")
    public Optional<MemberSave> getMemberSave(@Param("memberId") Long id);

    // 관심상품의 이름, 가격, 이미지 함께 조회하는 기능
    @Query("SELECT ms.id, cb.id, cb.name, cb.price, ci.fileName " +
            "FROM MemberSaveItem msi " +
            "JOIN msi.memberSave ms " +
            "JOIN msi.coffeeBean cb " +
            "JOIN cb.imageList ci " +
            "WHERE ms.member.id = :memberId")
    Page<Object[]> findSavedItemsWithImages(@Param("memberId") Long memberId, Pageable pageable);

}
