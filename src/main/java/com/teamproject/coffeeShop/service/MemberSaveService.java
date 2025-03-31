package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.CartCoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.CoffeeBeanDTO;
import com.teamproject.coffeeShop.dto.MemberSaveDTO;
import com.teamproject.coffeeShop.dto.MemberSaveListDTO;

import java.util.List;
// 관심상품Service
public interface MemberSaveService {
    // 관심상품 목록 조회
    List<MemberSaveListDTO> getListMemberSave(Long memberId);
    // 관심상품 추가
    Long addCoffeeBeans(MemberSaveDTO memberSaveDTO);
    // 관심상품 삭제
    void removeCoffeeBean(Long memberId, Long coffeeBeanId);
    // 관심상품 전체 삭제
    void removeAllCoffeeBean(Long memberId);
}
