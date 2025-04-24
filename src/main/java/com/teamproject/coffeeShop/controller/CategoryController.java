package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.domain.Category;
import com.teamproject.coffeeShop.dto.CategoryDTO;
import com.teamproject.coffeeShop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
/* 카테고리 컨트롤러 - 나영일 */
public class CategoryController {

    private final CategoryService categoryService;

    // 부모 카테고리 조회 (자식 카테고리 포함 : CategoryDTO(category, true))
    // 양방향 관계로 인하여 재귀적인 관계(부모-자식 트리 구조)인 경우 -> 그대로 반환하면 StackOverflowError가 발생할 수 있음.
    // -> 수동으로 변환 (ModelMapper 사용 X)
    @GetMapping("/parents")
    public ResponseEntity<List<CategoryDTO>> getParentCategories(){
        List<CategoryDTO> categories = categoryService.getParentCategories()
                .stream().map(category -> new CategoryDTO(category, true))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // 특정 부모 카테고리에 속한 자식 카테고리 목록 조회
    @GetMapping("/parents/{parentId}")
    public ResponseEntity<List<CategoryDTO>> getChildCategories(@PathVariable Long parentId) {
        List<CategoryDTO> categories = categoryService.getChildCategories(parentId)
                .stream().map(category -> new CategoryDTO(category, false))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // 상위 카테고리 등록
    @PostMapping("/addParent")
    public ResponseEntity<CategoryDTO> createParentCategory(@RequestParam String name){
        Category category = categoryService.addParentCategory(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryDTO(category, false));
    }

    // 하위 카테고리 등록
    @PostMapping("/addChild/{parentId}")
    public ResponseEntity<CategoryDTO> createChildCategory(@PathVariable Long parentId, @RequestParam String name){
        Category category = categoryService.addChildCategory(parentId, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryDTO(category, false));
    }

    // 카테고리 이름 수정
    @PutMapping("updateName/{categoryId}")
    public ResponseEntity<Void> updateCategoryName(@PathVariable Long categoryId, @RequestParam String name){
        categoryService.updateCategoryName(categoryId, name);
        return ResponseEntity.noContent().build();
    }

    // 부모 카테고리 수정
    @PutMapping("updateParent/{parentId}")
    public ResponseEntity<Void> updateParentCategory(@RequestParam Long id, @PathVariable Long parentId){
        categoryService.updateParentCategory(id, parentId);
        return ResponseEntity.noContent().build();
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
