package com.teamproject.coffeeShop.domain;

// 관심상품 엔티티 - 이재민
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_save_id")
    private Long id;

    @Setter
    private Long memberId;
    @Setter
    private Long cfbId;


    private Long cartId;

}
