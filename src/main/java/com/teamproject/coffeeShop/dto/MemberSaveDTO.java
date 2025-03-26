package com.teamproject.coffeeShop.dto;

import com.teamproject.coffeeShop.domain.MemberSave;

// 관심상품DTO - 이재민
public class MemberSaveDTO {

    private Long memberSaveId;
    private Long memberId;
    private Long cbfId;
    private Long cartId;

    public void MemberSaveDTO(MemberSave memberSave) {
        this.memberSaveId = memberSave.getId();
        this.memberId = memberSave.getId();
        this.cbfId = memberSave.getId();
        this.cartId = memberSave.getId();
    }
}
