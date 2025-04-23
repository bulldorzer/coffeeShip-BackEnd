package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.CoffeeBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/* 원두 Repository - 나영일 */
public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long>{

    // 원두 이름으로 원두 찾기
    List<CoffeeBean> findByName(String name);

    // 원두 고유번호로 원두 하나 선택
    @EntityGraph(attributePaths = "imageList")
    @Query("SELECT cb FROM CoffeeBean cb WHERE cb.id = :coffeeBeanId")
    Optional<CoffeeBean> selectOne(@Param("coffeeBeanId") Long coffeeBeanId);

    // 원두 + 원두 이미지 페이징 처리
    @Query("SELECT cb, cbi FROM CoffeeBean cb LEFT JOIN cb.imageList cbi ON cbi.ord = 0 WHERE cb.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);

    @Query("SELECT cb, cbi FROM CoffeeBean cb LEFT JOIN cb.imageList cbi ON cbi.ord = 0 WHERE cb.delFlag = false")
    List<Object[]> selectAll();

    // 고유번호가 맞는 원두의 delFlag 수정
    @Transactional
    @Modifying
    @Query("UPDATE CoffeeBean cb SET cb.delFlag = :flag WHERE cb.id = :coffeeBeanId")
    void updateToDelete(@Param("coffeeBeanId") Long coffeeBeanId, @Param("flag") Boolean flag);

}
