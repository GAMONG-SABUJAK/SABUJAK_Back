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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;
    private final RedisTemplate<String, String> redisTemplate;

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

        // Redis에 저장
        redisTemplate.opsForValue().set(refreshJwt, user.getId().toString(), 30, TimeUnit.DAYS);

        // HttpOnly 쿠키로 클라이언트에 전달
        ResponseCookie refreshCookie = ResponseCookie.from("refreshJwt", refreshJwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new JwtRes(accessJwt);
    }
}
