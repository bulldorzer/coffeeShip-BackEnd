package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.dto.CoffeeBeanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
/* 원두 서비스 인터페이스 - 나영일 */
public interface CoffeeBeanService {

    // 전체 원두 수 반환
    int getAllCoffeeBeansSize();

    // 전체 원두 목록 조회(페이징 포함)
    Page<CoffeeBeanDTO> getAllCoffeeBeans(Pageable pageable);

    // 고유번호로 원두 가져오기
    CoffeeBeanDTO getCoffeeBeanById(Long id);

    // 이름으로 원두 가져오기
    List<CoffeeBeanDTO> getCoffeeBeansByName(String name);

    // 받아온 원두를 저장
    Long saveCoffeeBean(CoffeeBeanDTO coffeeBeanDTO);

    // 고유번호로 원두를 삭제
    void deleteCoffeeBean(Long id);

    // 고유번호로 원두를 수정
    void updateCoffeeBean(Long id, CoffeeBeanDTO coffeeBeanDTO);

    // 고유번호가 존재하는지 확인
    boolean existsById(Long id);

}
