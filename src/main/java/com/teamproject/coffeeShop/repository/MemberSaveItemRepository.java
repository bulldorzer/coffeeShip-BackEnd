package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CartCoffeeBean;
import com.teamproject.coffeeShop.domain.MemberSaveItem;
import com.teamproject.coffeeShop.dto.CustomPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// 관심상품 목록Repository - 이재민
public interface MemberSaveItemRepository extends JpaRepository<MemberSaveItem, Long> {

    // 특정 회원의 관심상품 목록을 불러오기
    @Query("SELECT msi FROM MemberSaveItem msi WHERE msi.memberSave.member.id = :memberId")
    Page<MemberSaveItem> findItemsByMemberId(@Param("memberId") Long memberId, Pageable pageable);


    // 특정 회원 관심상품 목록에서 특정 상품 추가
    @Modifying
    @Query("INSERT INTO MemberSaveItem (memberSave, coffeeBean) " +
            "SELECT ms, cb FROM MemberSave ms " +
            "JOIN CoffeeBean cb ON cb.id = :coffeeBeanId " +
            "WHERE ms.member.id = :memberId")
    void addItemToMemberSave(@Param("memberId") Long memberId, @Param("coffeeBeanId") Long coffeeBeanId);

    // 관심상품 목록에 이미 있는지 확인하는 중복검사
    @Query("select count(msi) > 0 "
            + "from MemberSaveItem msi "
            + "inner join MemberSave ms on msi.memberSave = ms "
            + "where ms.member.id = :memberId "
            + "and msi.coffeeBean.id = :coffeeBeanId")
    boolean existsByMemberAndItem(@Param("memberId") Long memberId, @Param("coffeeBeanId") Long coffeeBeanId);

    // 특정 회원 관심상품 목록에서 특정 상품 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM MemberSaveItem msi WHERE msi.memberSave.id = :memberSaveId AND msi.coffeeBean.id = :coffeeBeanId")
    void deleteByMemberSaveAndItemId(@Param("memberSaveId") Long memberSaveId, @Param("coffeeBeanId") Long coffeeBeanId);

    // 특정 회원 관심상품 목록 상품 전체 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM MemberSaveItem msi WHERE msi.memberSave.id = :memberSaveId")
    void deleteAllByMemberSave(@Param("memberSaveId") Long memberSaveId);


}
