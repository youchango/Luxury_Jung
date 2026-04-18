package com.luxury.jung.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 프론트엔드(React/Vite)와 백엔드(Spring Boot) 간의
 * 교차 출처 정책(CORS)을 전역적으로 허용 처리하는 설정 클래스입니다.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 프론트엔드 Origin 목록 (로컬 개발 + 프로덕션 주소 모두 등재)
        config.setAllowedOrigins(List.of(
            "http://localhost:5173",  // Vite 개발 서버
            "http://localhost:80",    // 도커 Nginx 서빙
            "http://localhost"        // 도커 브라우저 단축 주소
        ));

        // 허용할 HTTP 메서드 (GET, POST, PUT, DELETE, PATCH)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 모든 요청 헤더를 허용 (Authorization 헤더 포함)
        config.setAllowedHeaders(List.of("*"));

        // 쿠키(또는 JWT 인증 토큰 헤더) 포함 전송 허용
        config.setAllowCredentials(true);

        // 브라우저가 preflight(OPTIONS) 결과를 캐싱하는 시간 (1시간)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); // 전체 API 경로에 적용
        return source;
    }
}
