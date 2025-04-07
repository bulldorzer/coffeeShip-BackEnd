package com.teamproject.coffeeShop.repository;


import com.teamproject.coffeeShop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
/* 카테고리 리포지토리 - 나영일 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // select c from Category c where c.name = :name; (delFlag = false)
    // 데이터 1개 : Optional / 데이터 여러개 or 중복 가능성 있음 : List
    public Optional<Category> findByNameAndDelFlagFalse(String name);

    // 특정 부모 카테고리(parentId)에 속한 자식 카테고리를 찾음. (delFlag = false)
    // 자식 카테고리 조회 시 부모 카테고리의 delFlag = false인 경우만 조회
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.delFlag = false AND c.parent.delFlag = false")
    public List<Category> findByParentIdAndDelFlagFalse(Long parentId);

    // 부모 카테고리가 없는 카테고리 -> 부모 카테고리를 찾음. (delFlag = false)
    public List<Category> findByParentIdIsNullAndDelFlagFalse();

    // 존재여부를 검사해주는 메서드 (delFlag = false)
    public Boolean existsByNameAndDelFlagFalse(String name);
}

// find~ : 일치하는 결과물을 찾아옴.
// exists~ : 일치하는 것 있으면 true, 없으면 false