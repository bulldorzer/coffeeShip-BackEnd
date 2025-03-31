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
 * 사용자의 인증 처리를 하기 위함
 */
@Service // 서비스 클래스 선언
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
    log.info("----------------loadUserByUsername-----------------------------");

    Member member = memberRepository.getWithRoles(username);

    if(member == null){
      throw new UsernameNotFoundException("Not Found");
    }
    // MemberDTO 타입으로 반환
    MemberDTO memberDTO = new MemberDTO(
            member.getEmail(),
            member.getPw(),
            member.getNickname(),
            member.isSocial(),
            member.getMemberRoleList()
                  .stream()
                  .map(memberRole -> memberRole.name()).collect(Collectors.toList()));

    log.info(memberDTO);

    return memberDTO;

  }
  
}
