package com.sabujak.gamong.config.security;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.exception.HandleJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtUtility {

    private final SecretKey secretKey; // JWT 서명에 사용되는 비밀 키 // 생성한 비밀 키의 타입이 SecretKey 타입

    private static final long accessJwtExpirationTime = 1000L * 60 * 60; // 밀리초 단위 // JWT 만료 시간: 1시간
    private static final long refreshJwtExpirationTime = 1000L * 60 * 60 * 24 * 30; // 밀리초 단위 // JWT 만료 시간: 1시간

    // JWT 서명에 사용되는 비밀 키 생성
    public JwtUtility(@Value("${jwt.base64Secret}") String base64Secret) { // @Value을 통해 application.yml에서 값 주입
        this.secretKey = Keys.hmacShaKeyFor(base64Secret.getBytes());
    }

    // JWT 토큰 생성
    public String generateAccessJwt(User user) {
        Instant now = Instant.now();
        return Jwts.builder() // JWT 빌더 초기화
                .claims() // Claims 설정
                .subject(String.valueOf(user.getId())) // 이메일을 JWT 토큰의 주체로 설정
                .issuedAt(Date.from(now)) // JWT 발행 시간 설정
                .expiration(Date.from(now.plusMillis(accessJwtExpirationTime))) // JWT 만료 시간 설정
                .and() // claims() 닫기
                .signWith(secretKey) // 지정된 알고리즘과 비밀키를 사용하여 JWT 토큰 서명
                .compact(); // JWT 문자열 생성
    }

    // JWT 토큰 유효성 검사
    public boolean validateJwt(String accessJwt) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessJwt); // 주어진 JWT 토큰 파싱하여 서명을 검증
            return true; // 올바르면 true 반환
        } catch (ExpiredJwtException e) {
            throw new HandleJwtException("만료된 JWT 형식");
        } catch (UnsupportedJwtException e) {
            throw new HandleJwtException("지원되지 않는 JWT 형식");
        } catch (MalformedJwtException e) {
            throw new HandleJwtException("손상된 JWT");
        } catch (SecurityException e) {
            throw new HandleJwtException("서명이 올바르지 않은 JWT");
        } catch (IllegalArgumentException e) {
            throw new HandleJwtException("JWT가 null이거나 빈 문자열임");
        } catch (JwtException e) {
            throw new HandleJwtException("기타 JWT관련 예외");
        }
    }

    // JWT 토큰에서 클레임을 추출하여 반환
    public Claims getClaimsFromAccessJwt(String accessJwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessJwt)
                .getPayload();  // JWT의 페이로드에서 클레임 반환
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder() // JWT 빌더 초기화
                .claims() // Claims 설정
                .subject(String.valueOf(user.getId())) // 이메일을 JWT 토큰의 주체로 설정
                .issuedAt(Date.from(now)) // JWT 발행 시간 설정
                .expiration(Date.from(now.plusMillis(refreshJwtExpirationTime))) // JWT 만료 시간 설정
                .and() // claims() 닫기
                .signWith(secretKey) // 지정된 알고리즘과 비밀키를 사용하여 JWT 토큰 서명
                .compact(); // JWT 문자열 생성
    }

    public String extractTokenFromCookies(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null; // 토큰 없으면 null 반환
    }

    public Claims getClaimsFromCookies(HttpServletRequest request, String cookieName) {
        String jwt = extractTokenFromCookies(request, cookieName);
        return getClaimsFromAccessJwt(jwt);
    }
}
