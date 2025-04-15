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
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO{

    // memeber 기본정보
    private Long memberId;
    private String email;
    private String name;
    private String pw;
    private String phone;
    private int point;

    // Address
    private String city;
    private String street;
    private String zipcode;

    // 소셜 로그인
    private boolean social;

    // 사용자 등급
    private String memberShip;

    // 사용자 권한
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
        this.social = member.isSocial();
        this.memberShip = member.getMemberShip().name();
        this.roleNames = (member.getMemberRoleList() == null)
                ? new ArrayList<>()
                : member.getMemberRoleList().stream()
                .map(Enum::name)// enum타입을 string 타입으로 변환
                .collect(Collectors.toList());
    }


    // 사용자 정보 저장
    public MemberDTO(String email, String pw, String name, boolean social, List<String> roleNames) {

        this.email = email;
        this.pw = pw;
        this.name = name;
        this.social = social;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims(){

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw",pw);
        dataMap.put("name", name);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }


    // 사용자 정보를 받아 Map형태로 변환하여 JWT토큰 생성 등에 사용
    public static Map<String, Object> getClaims(MemberDTO memberDTO) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", memberDTO.getEmail());
        dataMap.put("pw", memberDTO.getPw());
        dataMap.put("name", memberDTO.getName());
        dataMap.put("social", memberDTO.isSocial());
        dataMap.put("roleNames", memberDTO.getRoleNames());

        return dataMap;
    }

}
