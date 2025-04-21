package com.teamproject.coffeeShop.controller;

import com.google.gson.Gson;
import com.teamproject.coffeeShop.dto.LoginRequest;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.exception.CustomJWTException;
import com.teamproject.coffeeShop.security.MemberDetails;
import com.teamproject.coffeeShop.service.MemberService;
import com.teamproject.coffeeShop.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public void login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {

        String email = loginRequest.get("email");
        String pw = loginRequest.get("pw");
        log.info("--- [AuthController] --- 매개변수 정보 : email: {}, password: {}", email, pw);

        try {
            // 사용자가 입력한 email과 password를 기반으로 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, pw);


            // CustomUserDetailsService를 통해 DB에서 사용자 정보 로드 후 검증
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("--- [AuthController] --- 인증성공 : " + email);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();

//            if (!passwordEncoder.matches(password, memberDetails.getPassword())) {
//                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
//            }

            MemberDTO memberDTO = memberDetails.toMemberDTO();

            Map<String, Object> claims = memberDTO.getClaims(); // 원본 객체 참조

            Map<String, Object> responseMap = new HashMap<>(claims); // 원본 객체 복사하여 원본 객체 값 보존
            responseMap.put("accessToken", jwtUtil.generateToken(claims, 10));
            responseMap.put("refreshToken", jwtUtil.generateToken(claims, 60 * 24));

            // Gson을 이용해 응답 데이터를 JSON 형식으로 변환 후 반환
            String jsonStr = new Gson().toJson(responseMap);

            response.setContentType("application/json;charset=UTF-8"); // 한글 인코딩
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(jsonStr);

        } catch (BadCredentialsException e) {
            log.error("❌ --- [AuthController] --- 이메일 또는 비밀번호 오류 : " + email, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("❌ --- [AuthController] --- 로그인 과정 중 예기치 못한 오류 ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping("/refresh")
    /*
       - `@RequestHeader("Authorization")` → 요청 헤더에서 `accessToken` 추출
       - `@RequestParam String refreshToken` → 요청 파라미터에서 `refreshToken` 추출
    */
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String refreshToken) {

        if(refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if(authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        if(!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer)claims.get("exp"))
                ? jwtUtil.generateToken(claims, 60 * 24)
                : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    // 액세스 토큰 만료 여부 확인
    private boolean checkExpiredToken(String token) {
        try {
            jwtUtil.validateToken(token);
        } catch(CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }

    // 리프레시 토큰 갱신 여부 확인
    private boolean checkTime(Integer exp) {
        java.util.Date expDate = new java.util.Date((long) exp * 1000);
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);
        return leftMin < 60;
    }
}
