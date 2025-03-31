package com.teamproject.coffeeShop.repository;

// 관심상품Repository - 이재민
import com.teamproject.coffeeShop.domain.MemberSave;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MemberSaveRepository extends JpaRepository<MemberSave, Long> {
    // 특정 회원의 관심 상품 목록 조회
    public List<MemberSaveListDTO> getListOfMemberSaveById(@Param("memberId") Long memberId);

    // 특정 사용자의 관심목록에 특정 상품이 이미 있는지 확인(관심상품에 있으면 true / 없으면 false 반환)
    public boolean existsByMemberIdAndCfbId(@Param("memberId") Long id, @Param("cfbId") Long cfbId);

    // 특정 회원의 관심 상품 삭제
    public void deleteByMemberIdAndCfbId(@Param("memberId") Long id, @Param("cfbId") Long cfbId);

    // 특정 회원의 관심상품 전부 삭제
    public void deleteByMemberId(@Param("memberId") Long id);
}
