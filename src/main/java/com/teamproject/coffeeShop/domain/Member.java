package com.teamproject.coffeeShop.domain;

// 회원 엔티티 - 이재민
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String pw;
    private String phone;

    private int point;

    private String city;
    private String street;
    private String zipcode;

    // true : 소셜(카카오) 로그인/회원가입, false : 일반 로그인/회원가입
    private boolean social;

    @Enumerated(EnumType.STRING)
    private MemberShip memberShip;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList();

    public void addRole(MemberRole memberRole){
        if(this.memberRoleList == null) {
            this.memberRoleList = new ArrayList<>();
        }
        memberRoleList.add(memberRole);
    }

    public void clearRole(){
        memberRoleList.clear();
    }


    public void changeMemberShip(MemberShip memberShip){
        this.memberShip = memberShip;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePw(String pw) {
        this.pw = pw;
    }

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changePoint(int point) {
        this.point = point;
    }

    public void changeAddress(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
