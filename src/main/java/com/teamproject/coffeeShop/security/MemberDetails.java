package com.teamproject.coffeeShop.security;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.dto.MemberDTO;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// 사용자 인증 정보 클래스
@Getter
public class MemberDetails implements UserDetails {

    private final String email;
    private final String pw;
    private final String name;
    private final boolean social;
    private final List<GrantedAuthority> authorities;

    // GrantedAuthority : 인터페이스 -> SimpleGrantedAuthority : 구현클래스
    // 사용자의 권한을 저장하는 인터페이스와 구현클래스다
    // 저장할 때 권한에 반드시 "ROLE_" 글자가 있어야함 EX) USER -> ROLE_USER 형태
    public MemberDetails(Member member){
        this.email = member.getEmail();
        this.pw = member.getPw();
        this.name = member.getName();
        this.social = member.isSocial();
        this.authorities = member.getMemberRoleList().stream().map(role -> new SimpleGrantedAuthority("ROLE_"+ role)).collect(Collectors.toList());
    }

    public MemberDetails(String email, String pw, String name, boolean social, List<GrantedAuthority> authorities) {

        // Authentication (인증) vs Authorization (권한)
        // 사용자 인증 정보는 아래 3가지는 기본
        this.email = email;
        this.pw = pw;
        this.name = name;
        this.authorities = authorities; // 권한목록

        // 필요에 따라 추가 (DTO 또는 Entity에 들어갈 정보들)
        this.social = social;
    }

    // MemberDetails -> MemberDTO로 변환 메서드
    public MemberDTO toMemberDTO(){
        return new MemberDTO(
                this.email,
                this.pw,
                this.name,
                this.social,
                this.authorities.stream()
                        .map(GrantedAuthority::getAuthority)// 권한을 문자열로 가져오기
                        .map(role -> role.replace("ROLE_",""))// ROLE_ 글자 제거
                        .collect(Collectors.toList())// List 타입
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return email;
    }

    //계정이 만료되 었는가? 만료x - true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 상태 - 잠김x - true
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 인증 자격 증명(ex. 비밀번호), 인증정보 유효 = true
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 - 활성화 0 -  true
    @Override
    public boolean isEnabled() {
        return true;
    }
}
