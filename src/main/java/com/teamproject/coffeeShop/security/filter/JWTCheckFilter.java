package com.teamproject.coffeeShop.security.filter;

import com.google.gson.Gson;
import com.teamproject.coffeeShop.dto.MemberDTO;
import com.teamproject.coffeeShop.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    // Preflight요청은 체크하지 않음 
    if(request.getMethod().equals("OPTIONS")){
      return true;
    }

    String path = request.getRequestURI();

    log.info("check uri.............." + path);
//    진우 가 일부러 잠궈둠
//    /* 서비스하면서 토큰체크 하지 않는 서비스들 */
//    if (path.equals("/api/coffeeBeans") || path.startsWith("/api/coffeeBeans")) {
//      return true;
//    }
//
//    if (path.equals("/api/review") || path.startsWith("/api/review")) {
//      return true;
//    }
//
//    if (path.equals("/api/cfaq") || path.startsWith("/api/cfaq")) {
//      return true;
//    }
//
//    if (path.equals("/api/pfaq") || path.startsWith("/api/pfaq")) {
//      return true;
//    }
//
//    /* 추후 지워야 할 경로들*/
//    if (path.equals("/api/members") || path.startsWith("/api/members")) {
//      return true;
//    }
//    if (path.equals("/api/membersave") || path.startsWith("/api/membersave")) {
//      return true;
//    }
//    if (path.equals("/api/deliveries") || path.startsWith("/api/deliveries")) {
//      return true;
//    }
//    if (path.equals("/api/cart") || path.startsWith("/api/cart")) {
//      return true;
//    }
//    if (path.equals("/api/orders") || path.startsWith("/api/orders")) {
//      return true;
//    }

// false로 바꿔야함
    return true;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    log.info("------------------------JWTCheckFilter.......................");

    String authHeaderStr = request.getHeader("Authorization");

    try {
      //Bearer accestoken...
      String accessToken = authHeaderStr.substring(7);

      Map<String, Object> claims = JWTUtil.validateToken(accessToken);

      log.info("JWT claims: " + claims);

      //filterChain.doFilter(request, response); //이하 추가

      String email = (String) claims.get("email");
      String pw = (String) claims.get("pw");
      String name = (String) claims.get("name");
      Boolean social = (Boolean) claims.get("social");
      List<String> roleNames = (List<String>) claims.get("roleNames");

      MemberDTO memberDTO = new MemberDTO(email, pw, name, social, roleNames);

      log.info("-----------------------------------");
      log.info(memberDTO);
      log.info(memberDTO.getAuthorities());

      UsernamePasswordAuthenticationToken authenticationToken
      = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);

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
