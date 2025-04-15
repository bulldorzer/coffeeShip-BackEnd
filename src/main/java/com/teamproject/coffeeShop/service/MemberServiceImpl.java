package com.teamproject.coffeeShop.service;

import com.teamproject.coffeeShop.domain.Member;
import com.teamproject.coffeeShop.domain.MemberRole;
import com.teamproject.coffeeShop.domain.MemberShip;
import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.exception.NoDataFoundException;
import com.teamproject.coffeeShop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// 회원Service 구현클래스 - 이재민
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Long createMember(MemberDTO memberDTO){
        validateDuplicateMember(memberDTO);
        Member member = Member.builder()
                .email(memberDTO.getEmail())
                .pw(passwordEncoder.encode(memberDTO.getPw()))
                .name(memberDTO.getName())
                .phone(memberDTO.getPhone())
                .point(memberDTO.getPoint())
                .city(memberDTO.getCity())
                .street(memberDTO.getStreet())
                .zipcode(memberDTO.getZipcode())
                .social(memberDTO.isSocial())
                .memberShip(MemberShip.BRONZE)
                .memberRoleList(memberDTO.getRoleNames().stream()
                        .map(MemberRole::valueOf)
                        .collect(Collectors.toList())).build();
        memberRepository.save(member);
        return member.getId();
    }

    @Override
    public CustomPage<MemberDTO> getAllMembersPaged(Pageable pageable){
        Page<Member> memberPage = memberRepository.findAll(pageable);
        if(memberPage == null) {
            throw new NoDataFoundException("조회된 데이터 없음");
        }
        Page<MemberDTO> dtoPage = memberPage.map(member -> modelMapper.map(member, MemberDTO.class));

        int groupSize = 10;
        return CustomPage.of(dtoPage, groupSize);
    }

    @Override
    public MemberDTO getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Member Not Found"));
        return modelMapper.map(member, MemberDTO.class);
    }

    @Transactional
    @Override
    public void deleteMember(Long id) {
        if(memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
        } else {
            throw new RuntimeException("Member Not Found");
        }
    }

    @Transactional
    @Override
    public void updateMember(Long id, MemberDTO memberDTO) {
        Member searchMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member Not Found"));

        if (!searchMember.getEmail().equals(memberDTO.getEmail())) {
            validateDuplicateMember(memberDTO);
            searchMember.changeEmail(memberDTO.getEmail());
        }

        searchMember.changeName(memberDTO.getName());
        searchMember.changePw(memberDTO.getPw());
        searchMember.changePhone(memberDTO.getPhone());
        searchMember.changePoint(memberDTO.getPoint());
        searchMember.changeAddress(memberDTO.getCity(), memberDTO.getStreet(), memberDTO.getZipcode());
    }

    @Override
    public boolean existsById(Long id) {
        return memberRepository.existsById(id);
    }

    // 회원가입 임시 코드
    @Override
    public boolean login(String email, String pw) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        if (!member.getPw().equals(pw)) {
            throw new RuntimeException("비밀번호가 일치하지 않음");
        }
        return true;
    }


    private void validateDuplicateMember(MemberDTO memberDTO) {
        Optional<Member> foundMember = memberRepository.findByEmail(memberDTO.getEmail());
        if(foundMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원");
        }
    }


}
