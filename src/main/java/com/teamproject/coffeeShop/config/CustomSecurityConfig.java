package com.teamproject.coffeeShop.config;

import com.teamproject.coffeeShop.security.filter.JWTCheckFilter;
import com.teamproject.coffeeShop.security.handler.APILoginFailHandler;
import com.teamproject.coffeeShop.security.handler.APILoginSuccessHandler;
import com.teamproject.coffeeShop.security.handler.CustomAccessDeniedHandler;
import com.teamproject.coffeeShop.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

  private final JWTUtil jwtUtil;



  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    log.info("---------------------security config---------------------------");

    http
          // 1. CORS 정책적용
          .cors(cors -> cors.configurationSource(configurationSource()))
          // 2. 세션 사용 안함 -> 토큰 발행
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          // URL 별 접근 제어 설정
          .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/api/members/**").permitAll() // 로그인 화면
                  .requestMatchers("/api/membersave/**").permitAll() // 관심상품 화면
                  .requestMatchers("/api/deliveries/**").permitAll() // 배달여부 화면
                  .requestMatchers("/api/cart/**").permitAll() // 장바구니 화면
                  .requestMatchers("/api/coffeeBeans/**").permitAll() // 상품화면
                  .requestMatchers("/api/orders/**").permitAll() //  주문서 화면
                  .requestMatchers("/api/review/**").permitAll() //  주문후기 화면
                  .requestMatchers("/api/cfaq/**").permitAll() //  고객문의 화면
                  .requestMatchers("/api/pfaq/**").permitAll() //  상품문의 화면
                  .requestMatchers("/api/categories/**").permitAll() // 카테고리 목록
                  .requestMatchers("/api/admin/**").hasAnyRole("MANAGER", "ADMIN") // 관리자 화면
                  .anyRequest().authenticated()
          )
          // 사용자 정보 인증 전에 토큰 체크 부터 시행
          .addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
          // 3. CSRF 비활성화 (세션을 사용 안하기 때문)
          .csrf(csrf -> csrf.disable())
          // HTTP Basic 인증 비활성화, HTTP Basic 인증은 브라우저에서 기본 제공하는 로그인 방식 (팝업 창)
          .httpBasic(httpBasic -> httpBasic.disable())
          // 기본 폼 로그인 비활성화
          .formLogin(form -> form.disable())
          // 6 - 권한 예외 처리(접근 거부 예외핸들러 설정)
          .exceptionHandling(exception -> exception.accessDeniedHandler(new CustomAccessDeniedHandler()));

    return http.build();
  }

  //AuthenticationManager 인증 매니저 등록
  /*
    Spring Security에서 사용자 인증을 처리하는 핵심 인터페이스로,
    로그인 요청이 들어오면 **사용자의 자격 증명(아이디, 비밀번호 등)이 올바른지 확인하고 인증을 처리**하는 역할을 한다.
  */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
    throws Exception{
      return authenticationConfiguration.getAuthenticationManager();
  }


  @Bean
  public CorsConfigurationSource configurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
    // 허용할 HTTP 메서드 설정
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
    // 요청 헤더 허용
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    // 자격 증명 허용
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }


  // 비밀번호 인코딩
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
  
}
