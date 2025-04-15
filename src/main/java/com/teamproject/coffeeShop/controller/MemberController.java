package com.teamproject.coffeeShop.controller;

import com.teamproject.coffeeShop.dto.CustomPage;
import com.teamproject.coffeeShop.dto.LoginRequest;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.repository.MemberRepository;
import com.teamproject.coffeeShop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 회원Controller - 이재민
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final MemberService memberService;

    // 전체 회원 조회
    @GetMapping("/list")
    public ResponseEntity<CustomPage<MemberDTO>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(memberService.getAllMembersPaged(pageable));
    }

    // 특정 회원 조회
    @GetMapping("{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMember(id));
    }

    // 회원 생성
    @PostMapping
    public ResponseEntity<MemberDTO> createMember(@RequestBody MemberDTO memberDTO){
        Long memberId = memberService.createMember(memberDTO);
        return ResponseEntity.ok(memberDTO);
    }

    // 회원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Long id, @RequestBody MemberDTO memberDTO) {
        memberService.updateMember(id, memberDTO);
        return ResponseEntity.ok(memberService.getMember(id));
    }

    // 회원 존재 여부 확인
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.existsById(id));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // 마이페이지에서 쓸 DTO 추출
    @GetMapping("/me/{email}")
    public ResponseEntity<MemberDTO> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

//    // 로그인 임시 코드
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        String email = loginRequest.getEmail();
//        String pw = loginRequest.getPw();
//
//        log.info("email ",email, " pw ",pw);
//        boolean isAuthenticated = memberService.login(email, pw);
//
//
//        if (isAuthenticated) {
//            return ResponseEntity.ok("로그인 성공");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호 틀림");
//        }
//    }
}
