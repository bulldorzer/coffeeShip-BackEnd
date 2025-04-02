package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.MemberSave;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 관심상품DTO - 이재민
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveDTO {

    private Long memberSaveId;
    private Long memberId;
    private Long coffeeBeanId;


}
