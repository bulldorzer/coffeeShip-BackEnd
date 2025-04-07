package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
/* 카테고리 DTO - 나영일 */
public class CategoryDTO {

    private Long id;
    private String name;
    private boolean delFlag;

    private Long parentId;
    private List<CategoryDTO> children;

    // 생성자 : includeChildren = false 이면 자식 제외 (true면 자식 포함)
    public CategoryDTO(Category category, boolean includeChildren) {
        this.id = category.getId();
        this.name = category.getName();
        this.delFlag = category.isDelFlag();
        this.parentId = category.getParent() != null    // NullPointerException 방지
                ? category.getParent().getId()
                : null;
        if (includeChildren) {
            this.children = category.getChild().stream()
                    .filter(child -> !child.isDelFlag()) // delFlag 고려
                    .map(child -> new CategoryDTO(child, true))
                    .collect(Collectors.toList());
        }
    }
}