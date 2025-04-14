package com.teamproject.coffeeShop.service;

//회원Service - 이재민


import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.MemberDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    Long createMember(MemberDTO memberDTO);
    CustomPage<MemberDTO> getAllMembersPaged(Pageable pageable);
    MemberDTO getMember(Long id);
    void deleteMember(Long id);
    void updateMember(Long id, MemberDTO memberDTO);
    // 회원 존재 여부 확인
    boolean existsById(Long id);
    // 로그인 임시 코드
    boolean login(String email, String pw);
}
