package com.teamproject.coffeeShop.security.handler;

import com.google.gson.Gson;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler{
  private final JWTUtil jwtUtil;
  private final MemberDTO memberDTO;

@Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    log.info("-------------------------------------");
    log.info(authentication); // 토큰정보와 계정 아이디, 비밀번호 그리고 권한정보 까지 확인 가능함
    log.info("-------------------------------------");

    MemberDTO memberDTO = (MemberDTO)authentication.getPrincipal();
    // 진우 작업 나중에 풀예정
    Map<String, Object> claims = memberDTO.getClaims();

    String accessToken = jwtUtil.generateToken(claims,10);
    String refreshToken = jwtUtil.generateToken(claims,60*24);

    claims.put("accessToken", accessToken); // 승인토큰
    claims.put("refreshToken", refreshToken); // 재승인토큰

    Gson gson = new Gson();

    String jsonStr = gson.toJson(claims);

    response.setContentType("application/json; charset=UTF-8");
    PrintWriter printWriter = response.getWriter();
    printWriter.println(jsonStr);
    printWriter.close();

  }

  
}
