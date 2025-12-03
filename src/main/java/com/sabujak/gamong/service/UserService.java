package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.exception.AlreadyLoginIdException;
import com.sabujak.gamong.exception.InvalidLoginIdException;
import com.sabujak.gamong.exception.InvalidPasswordException;
import com.sabujak.gamong.repository.UserRepository;
import com.sabujak.gamong.config.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${cookie.secure}")
    private boolean isSecure;

    @Value("${cookie.sameSite}")
    private String isSameSite;

    // 회원가입
    @Transactional
    public void signUp(ReqSignUp reqSignUp) {
        userRepository.findByLoginId(reqSignUp.loginId())
                .ifPresent(u -> { throw new AlreadyLoginIdException(); });

        User user = new User(
                reqSignUp.loginId(),
                reqSignUp.password(),
                reqSignUp.nickname(),
                reqSignUp.ceoName(),
                reqSignUp.businessNum(),
                reqSignUp.businessName(),
                reqSignUp.businessType(),
                reqSignUp.businessItem(),
                reqSignUp.businessAddress()
        );

        userRepository.save(user);
    }

    // 로그인
    public JwtRes login(HttpServletResponse response, ReqLogin reqLogin) {
        User user = userRepository.findByLoginId(reqLogin.loginId())
                .orElseThrow(InvalidLoginIdException::new);

        if (!user.checkPassword(reqLogin.password()))
            throw new InvalidPasswordException();

        String accessJwt = jwtUtility.generateAccessJwt(user);

        String refreshJwt = jwtUtility.generateRefreshToken(user);

        String redisKey = "refresh_jwt:" + refreshJwt;

        redisTemplate.opsForValue().set(redisKey, refreshJwt, 30, TimeUnit.DAYS);

        ResponseCookie refreshCookie = ResponseCookie.from("refresh_jwt", refreshJwt)
                .httpOnly(true)
                .secure(isSecure)
                .sameSite(isSameSite)
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new JwtRes(accessJwt);
    }

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshJwt = jwtUtility.extractTokenFromCookies(request, "refresh_jwt");

        if (refreshJwt != null) {
            redisTemplate.delete("refresh_jwt:" + refreshJwt);
        }

        ResponseCookie deleteRefreshJwtCookie = ResponseCookie.from("refresh_jwt", "")
                .httpOnly(true)
                .secure(isSecure)
                .sameSite(isSameSite)
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteRefreshJwtCookie.toString());

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}
