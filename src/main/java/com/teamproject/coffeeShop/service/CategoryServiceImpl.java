package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Category;
import com.teamproject.coffeeShop.dto.CategoryDTO;
import com.teamproject.coffeeShop.repository.CategoryCoffeeBeanRepository;
import com.teamproject.coffeeShop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
/* 카테고리 서비스 구현 클래스 - 나영일 */
public class CategoryServiceImpl implements CategoryService{

    public final CategoryRepository categoryRepository;
    public final CategoryCoffeeBeanRepository categoryCoffeeBeanRepository;

    @Override
    // 부모 카테고리 등록
    // 카테고리 이름을 매개변수로 받음. -> 이름 중복 검사 -> 카테고리 생성 및 이름 설정 -> 리포지토리에 저장
    public Category addParentCategory(String name) {

        checkDuplicateCategory(name);

        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

        return category;
    }

    // 카테고리명 중복 여부 검사 메서드
    public void checkDuplicateCategory(String name) {
        if (categoryRepository.existsByNameAndDelFlagFalse(name)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
    }

    @Override
    // 자식 카테고리 등록
    // 부모 카테고리 고유번호, 자식 카테고리 이름 매개변수로 받음.
    // 이름 중복 확인 -> 부모 카테고리 객체 조회 -> 자식 카테고리 생성 및 이름 설정 -> 자식-부모 관계 설정 -> 리포지토리에 저장
    public Category addChildCategory(Long parentId, String name) {

        checkDuplicateCategory(name);

        Category parent = categoryRepository.findById(parentId).orElseThrow(
                ()->new IllegalArgumentException("존재하지 않는 부모 카테고리입니다.")
        );

        Category category = new Category();
        category.setName(name);
        category.setParent(parent);

        categoryRepository.save(category);

        return category;
    }

    @Override
    // 부모 카테고리 조회
    // 부모 카테고리가 없는 카테고리 = 부모 카테고리
    public List<Category> getParentCategories() {
        return categoryRepository.findByParentIdIsNullAndDelFlagFalse();
    }

    @Override
    // 특정 부모 카테고리(parentId)에 속한 자식 카테고리 조회
    public List<Category> getChildCategories(Long parentId) {
        return categoryRepository.findByParentIdAndDelFlagFalse(parentId);
    }

    @Override
    @Transactional
    // 카테고리 이름 수정
    // 카테고리 고유번호, 카테고리 이름 매개변수로 받음.
    // 고유번호로 카테고리 엔티티 조회 -> 이름 설정 -> 리포지토리 저장
    public void updateCategoryName(Long id, String name) {

        checkDuplicateCategory(name);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        category.setName(name);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    // 부모 카테고리 수정
    // 카테고리 고유번호, 부모 카테고리 고유번호 매개변수로 받음.
    // 고유번호로 카테고리/부모 카테고리 엔티티 조회 -> 부모 카테고리 설정 -> 리포지토리 저장
    // category_id = id 인 카테고리의 parent_id 를 parentId로 변경
    public void updateParentCategory(Long id, Long parentId) {

        if (id.equals(parentId)) {
            throw new IllegalArgumentException("자기 자신을 부모 카테고리로 설정할 수 없습니다.");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 카테고리입니다."));

        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 부모 카테고리입니다."));

        category.setParent(parentCategory);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    // 카테고리 삭제
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));

        // 카테고리 삭제 시 연결된 원두도 함께 삭제
        categoryCoffeeBeanRepository.deleteByCategoryId(id);

        // Soft Delete
        category.setDelFlag(true);
    }
}
