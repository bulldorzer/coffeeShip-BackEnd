package com.teamproject.coffeeShop.config;

import com.teamproject.coffeeShop.security.filter.JWTCheckFilter;
import com.teamproject.coffeeShop.security.handler.APILoginFailHandler;
import com.teamproject.coffeeShop.security.handler.APILoginSuccessHandler;
import com.teamproject.coffeeShop.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

  // 비밀번호 인코딩
  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }


@Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    log.info("---------------------security config---------------------------");

    // 1. CORS 정책적용
    http.cors(httpSecurityCorsConfigurer -> {
      httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
    });

    // 2. 세션 사용 안함 -> 토큰 발행
    http.sessionManagement(sessionConfig ->  sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // 3. CSRF 비활성화 (세션을 사용 안하기 때문)
    http.csrf(config -> config.disable());

    http.formLogin(config -> {
      config.loginPage("/api/member/login"); // 로그인 페이지 매핑
      config.successHandler(new APILoginSuccessHandler()); // 로그인 후처리
      config.failureHandler(new APILoginFailHandler()); // 로그인 실패 처리
    });

    http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); //JWT체크

    http.exceptionHandling(config -> {
      config.accessDeniedHandler(new CustomAccessDeniedHandler());
    });


  // URL 별 접근 제어 설정
  http.authorizeHttpRequests(authz -> {
    authz
            .requestMatchers("/api/members/**").permitAll() // 로그인 화면
            .requestMatchers("/api/membersave/**").permitAll() // 로그인 화면
            .requestMatchers("/api/deliveries/**").permitAll() // 배달여부
            .requestMatchers("/api/cart/**").permitAll() // 배달여부
            .requestMatchers("/api/coffeeBeans/**").permitAll() // 배달여부
            .requestMatchers("/api/orders/**").permitAll() //  주문목록
            .requestMatchers("/api/categories/**").permitAll() // 카테고리 목록
            .requestMatchers("/api/admin/**").hasAnyRole("MANAGER", "ADMIN") // 관리자 화면
            .anyRequest().authenticated();
  });


  return http.build();
  }




    @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
  
}
