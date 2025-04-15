package com.teamproject.coffeeShop.security;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.stream.Collectors;

/**
 * CustomUSerDetailsService
 * Spring Security의 UserDetailsService를 구현하여 → 사용자 인증 정보를 조회하는 서비스
 */
@Service // 서비스 클래스 선언
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    log.info("----------------loadUserByUsername-----------------------------");
    log.info("email: {}",email);

    try {

      Member member = memberRepository.getWithRoles(email);
      log.info("member {}",member);
      return new MemberDetails(member);

    } catch (UsernameNotFoundException e) {

      log.error("사용자를 찾을 수 없음: {}", email, e);
      throw e;

    }




//    if(member == null){
//      throw new UsernameNotFoundException("Not Found");
//    }
    // MemberDTO 타입으로 반환
//    MemberDTO memberDTO = new MemberDTO(
//            member.getEmail(),
//            member.getPw(),
//            member.getName(),
//            member.isSocial(),
//            member.getMemberRoleList()
//                  .stream()
//                  .map(memberRole -> memberRole.name()).collect(Collectors.toList()));
//
//    log.info("memberDTO ",memberDTO);
//    return memberDTO;


  }
  
}
