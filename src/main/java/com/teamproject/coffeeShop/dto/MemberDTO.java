package com.teamproject.coffeeShop.dto;

// 회원DTO - 이재민
import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.MemberShip;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(exclude = "pw")
public class MemberDTO extends User {
    private Long memberId;
    private String email;
    private String name;
    private String pw;
    private String phone;
    private int point;
    private String city;
    private String street;
    private String zipcode;
    private boolean social;
    private MemberShip memberShip;
    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(Member member){
        super(member.getEmail(), member.getPw(), member.getMemberRoleList().stream().map(str ->
                new SimpleGrantedAuthority("ROLE_"+str)).collect(Collectors.toList()));
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.pw = member.getPw();
        this.phone = member.getPhone();
        this.point = member.getPoint();
        this.city = member.getCity();
        this.street = member.getStreet();
        this.zipcode = member.getZipcode();
        this.social = member.isSocial();
        this.memberShip = member.getMemberShip();
        this.roleNames = (member.getMemberRoleList() == null)
                ? new ArrayList<>()
                : member.getMemberRoleList().stream().map(Enum::name).collect(Collectors.toList());
    }

    public MemberDTO(String email, String pw, String name, boolean social, List<String> roleNames) {
        super(email, pw, roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str))
                .collect(Collectors.toList()));
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.social = social;
        this.roleNames = roleNames;
    }

    // 사용자 정보를 받아 Map형태로 변환하여 JWT토근 생성등에 사용
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("name", name);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;

    }
}
