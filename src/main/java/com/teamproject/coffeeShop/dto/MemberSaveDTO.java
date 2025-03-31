package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.MemberSave;
import lombok.Getter;

// 관심상품DTO - 이재민
@Getter
public class MemberSaveDTO {

    private Long memberSaveId;
    private Long memberId;
    private Long cfbId;
    private Long cartId;

    public void MemberSaveDTO(MemberSave memberSave) {
        this.memberSaveId = memberSave.getId();
        this.memberId = memberSave.getId();
        this.cfbId = memberSave.getId();
        this.cartId = memberSave.getId();
    }
}
