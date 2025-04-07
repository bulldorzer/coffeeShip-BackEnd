package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Category;
import com.teamproject.coffeeShop.dto.CategoryDTO;

import java.util.List;

/* 카테고리 서비스 인터페이스 - 나영일 */
public interface CategoryService {

    Category addParentCategory(String name);                // 부모 카테고리 등록
    Category addChildCategory(Long parentId, String name);  // 자식 카테고리 등록

    List<Category> getParentCategories();               // 부모 카테고리만 조회
    List<Category> getChildCategories(Long parentId);   // 특정 부모 카테고리에 속한 자식 카테고리 조회

    void updateCategoryName(Long id, String name);      // 카테고리 이름 수정
    void updateParentCategory(Long id, Long parentId);    // 부모 카테고리 수정

    void deleteCategory(Long id);   // 카테고리 삭제
}
