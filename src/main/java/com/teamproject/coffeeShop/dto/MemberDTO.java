package com.teamproject.coffeeShop.dto;

// 회원DTO - 이재민
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.MemberShip;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "pw")
public class MemberDTO {
    private Long memberId;
    private String email;
    private String name;
    private String pw;
    private String phone;
    private int point;
    private String city;
    private String street;
    private String zipcode;
    private MemberShip memberShip;
    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(Member member){
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.pw = member.getPw();
        this.phone = member.getPhone();
        this.point = member.getPoint();
        this.city = member.getCity();
        this.street = member.getStreet();
        this.zipcode = member.getZipcode();
        this.memberShip = member.getMemberShip();
        this.roleNames = (member.getMemberRoleList() == null)
                ? new ArrayList<>()
                : member.getMemberRoleList().stream().map(Enum::name).collect(Collectors.toList());
    }

}
