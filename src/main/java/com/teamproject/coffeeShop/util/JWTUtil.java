package com.teamproject.coffeeShop.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Log4j2
public class JWTUtil {

  private static String key = "1234567890123456789012345678901234567890";

  // JWT 문자열 생성 = 토큰 생성
  public static String generateToken(Map<String, Object> valueMap, int min) {

    SecretKey key = null;

    try {
      key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }

    String jwtStr = Jwts.builder().header()
            .add("typ", "JWT")
            .add("alg", "HS256")
            .and()
            .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .expiration((Date.from(ZonedDateTime.now()
                    .plusMinutes(min).toInstant()))).claims(valueMap)
            .signWith(key)
            .compact();
    return jwtStr;
  }

  // 토큰 유효성 검사
  public static Map<String, Object> validateToken(String token) {

    Claims claims = null;
    
    try{

      SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

       claims = Jwts.parser().verifyWith(key)
      .build()
      .parseSignedClaims(token)// 파싱 및 검증, 실패 시 에러
      .getPayload();
              
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
    return claims;
  }

}
