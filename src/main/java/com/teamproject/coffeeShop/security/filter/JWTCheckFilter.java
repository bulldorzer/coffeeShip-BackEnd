package com.teamproject.coffeeShop.security.filter;

import com.google.gson.Gson;
import com.teamproject.coffeeShop.security.MemberDetails;
import com.teamproject.coffeeShop.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
  - Spring Security에서 **JWT 기반 인증을 처리하는 필터(보안문)**
  - 클라이언트가 보낸 **JWT를 검증하고 인증 객체를 생성하여 SecurityContext에 저장**
  - `OncePerRequestFilter`를 상속하여 **모든 요청마다 한 번씩 실행**
  - 로그인, 특정 API 요청 등 **필터가 필요 없는 경우를 예외 처리**
*/
@Log4j2
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil; // 토큰 생성,검증

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    log.info("----------shouldNotFilter-----------");


    String path = request.getServletPath();

    log.info("check uri.............." + path);
//    진우 가 일부러 잠궈둠
    /* 서비스하면서 토큰체크 하지 않는 서비스들 */
    if (path.equals("/api/coffeeBeans") || path.startsWith("/api/coffeeBeans")) {
      return true;
    }

    if (path.equals("/api/review") || path.startsWith("/api/review")) {
      return true;
    }

    if (path.equals("/api/cfaq") || path.startsWith("/api/cfaq")) {
      return true;
    }

    if (path.equals("/api/pfaq") || path.startsWith("/api/pfaq")) {
      return true;
    }
    if (path.equals("/api/members") || path.startsWith("/api/members")) {
      return true;
    }

    /* 추후 지워야 할 경로들*/
    if (path.equals("/api/membersave") || path.startsWith("/api/membersave")) {
      return true;
    }
    if (path.equals("/api/deliveries") || path.startsWith("/api/deliveries")) {
      return true;
    }
    if (path.equals("/api/cart") || path.startsWith("/api/cart")) {
      return true;
    }
    if (path.equals("/api/orders") || path.startsWith("/api/orders")) {
      return true;
    }

    // 브라우저->서버 요청 보내기 전에
    // 보안 때문에, 내 요청 수락 가능?
    // 이런 확인요청을 OPTIONS 요청이라고 함.  = 프리플라이트
    // PUT, DELETE 수정, 삭제 하는 요청일 경우에 사전에 보냄 = 사전체크
    if(request.getMethod().equals("OPTIONS")){
      return true;
    }

// false로 바꿔야함
    return false;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    log.info("------------------------JWTCheckFilter.......................");

    String authHeaderStr = request.getHeader("Authorization");

    if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
      log.info("----------No JWT Token Found, skipping filter-----------");
      filterChain.doFilter(request, response);
      return;
    }

    try {
      // 인증 방식 : token에 저장된 값을 추출하여 인증 객체 생성
      // 토큰 추출 -> 사용자 정보 추출 -> MemberDetails 변환 ->  인증객체 생성 -> 저장
      String accessToken = authHeaderStr.substring(7);

      Map<String, Object> claims = jwtUtil.validateToken(accessToken);

      log.info("JWT claims: " + claims);

      // 사용자 정보 추출
      String email = (String) claims.get("email");
      String pw = (String) claims.get("pw");
      String name = (String) claims.get("name");
      Boolean social = (Boolean) claims.get("social");
      List<String> roleNames = (List<String>) claims.get("roleNames");

      // Spring Security 권한 변환
      List<GrantedAuthority> authorities = roleNames.stream()
                      .map(role-> new SimpleGrantedAuthority("ROLE_" + role))
                              .collect(Collectors.toList());

      // MemberDetails 객체를 생성
      MemberDetails memberDetails = new MemberDetails(email,pw,name,social,authorities);

      log.info("-----------------------------------");
      log.info(memberDetails);

      // Authentication 객체를 생성하여 SecurityContext에 저장 (중요) 사용자정보와, 권한 등록
      // UsernamePasswordAuthenticationToken을 생성하여 Spring Security에서 인증된 사용자로 설정
      // SecurityContextHolder에 인증 정보를 저장하여 이후 컨트롤러에서 인증된 사용자로 인식
      UsernamePasswordAuthenticationToken authenticationToken
      = new UsernamePasswordAuthenticationToken(memberDetails,null,authorities);

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

      // 필터체인 진행
      filterChain.doFilter(request, response);

    }catch(Exception e){

      log.error("JWT Check Error..............");
      log.error(e.getMessage());

      Gson gson = new Gson();
      String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

      response.setContentType("application/json");
      PrintWriter printWriter = response.getWriter();
      printWriter.println(msg);
      printWriter.close();

    }
  }


}
