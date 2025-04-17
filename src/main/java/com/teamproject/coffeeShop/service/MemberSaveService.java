package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
// 관심상품Service
public interface MemberSaveService {
    // 관심상품 목록 조회
    CustomPage<MemberSaveListDTO> getListMemberSave(Long id, Pageable pageable);
    // 관심상품 추가
    Long addCoffeeBeans(MemberSaveDTO memberSaveDTO);
    // 관심상품 삭제
    void deleteCoffeeBean(Long memberId, List<Long> cfbIds);
    // 관심상품 전체 삭제
    void deleteAllCoffeeBean(Long memberId);
}
