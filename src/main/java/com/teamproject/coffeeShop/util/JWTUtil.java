package com.teamproject.coffeeShop.util;

import com.teamproject.coffeeShop.exception.CustomJWTException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

// JWT 기반 인증을 처리하는 핵심 역할 수행
@Log4j2
@Component
public class JWTUtil {

  private SecretKey key;

  // SecretKey 설정 생성자에서 불러오기
  public JWTUtil(@Value("${jwt.secret}") String secretKey){
    log.info("---------JWTUtil---------");
    byte[] decodedKey  = Base64.getDecoder().decode(secretKey);
    if (decodedKey.length < 32){
      throw new IllegalArgumentException("JWT Secret Key 32 bytes 이상이여야함.");
    }
    this.key = Keys.hmacShaKeyFor(decodedKey);
  }

  // JWT 문자열 생성 = 토큰 생성
  public String generateToken(Map<String, Object> valueMap, int min) {

    try {
      return Jwts.builder()
              .setHeader(Map.of("typ", "JWT"))
              .setClaims(valueMap)
              .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
              .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
              .signWith(key)
              .compact();

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

  }

  // 토큰 유효성 검사
  public Map<String, Object> validateToken(String token) {

    try{

      return Jwts.parserBuilder()
              .setSigningKey(key) // 서명검증
              .build()
              .parseClaimsJws(token)// JWT 파싱 및 검증
              .getBody();


    }catch(MalformedJwtException malformedJwtException){
        throw new CustomJWTException("MalFormed");
    }catch(ExpiredJwtException expiredJwtException){
        throw new CustomJWTException("Expired");
    }catch(InvalidClaimException invalidClaimException){
        throw new CustomJWTException("Invalid");
    }catch(JwtException jwtException){
        throw new CustomJWTException("JWTError");
    }catch(Exception e){
        throw new CustomJWTException("Error");
    }

  }

}
