package com.teamproject.coffeeShop.repository;

import com.teamproject.coffeeShop.domain.Category;
import com.teamproject.coffeeShop.domain.CategoryCoffeeBean;
import com.teamproject.coffeeShop.domain.CoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/* 카테고리 원두(상품) 연결 리포지토리 - 나영일 */
public interface CategoryCoffeeBeanRepository  extends JpaRepository<CategoryCoffeeBean, Long> {

    // 상품(원두) -> 카테고리 찾기
    @Query("SELECT ccb.category FROM CategoryCoffeeBean ccb WHERE ccb.coffeeBean.id = :coffeeBeanId")
    List<Category> findCategoriesByCoffeeBeanId(@Param("coffeeBeanId") Long coffeeBeanId);

    // 카테고리 -> 상품(원두) 찾기
    @Query("SELECT ccb.coffeeBean FROM CategoryCoffeeBean ccb WHERE ccb.category.id = :categoryId")
    List<CoffeeBean> findCoffeeBeansByCategoryId(@Param("categoryId") Long categoryId);

    // 존재 여부 확인 (중복 연결 방지용)
    boolean existsByCoffeeBeanIdAndCategoryId(Long coffeeBeanId, Long categoryId);

    // 원두 삭제 시 카테고리 연결도 함께 삭제
    void deleteByCoffeeBeanId(Long coffeeBeanId);

    // 카테고리 삭제 시 연결된 원두도 함께 삭제
    void deleteByCategoryId(Long categoryId);

}
