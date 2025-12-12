package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqBizStatus;
import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.dto.Response.KakaoAddressRes;
import com.sabujak.gamong.exception.AlreadyLoginIdException;
import com.sabujak.gamong.exception.HandleJwtException;
import com.sabujak.gamong.exception.InvalidLoginIdException;
import com.sabujak.gamong.exception.InvalidPasswordException;
import com.sabujak.gamong.repository.UserRepository;
import com.sabujak.gamong.security.JwtUtility;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Value("${nts.business.service-key}")
    private String businessServiceKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final WebClient kakaoWebClient;

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
                reqSignUp.businessAddress(),
                reqSignUp.latitude(),
                reqSignUp.longitude()
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
    // refreshJwt 재발급

    @Transactional
    public JwtRes reissuedRefreshJwt(HttpServletRequest request) {

        String refreshJwt = jwtUtility.extractTokenFromCookies(request, "refresh_jwt");

        if (refreshJwt == null) {
            throw new HandleJwtException("Refresh Token 없음");
        }

        String redisKey = "refresh_jwt:" + refreshJwt;

        String redisRefreshJwt = redisTemplate.opsForValue().get(redisKey);

        if (redisRefreshJwt == null || !redisRefreshJwt.equals(refreshJwt)) {
            throw new HandleJwtException("유효하지 않은 Refresh Token");
        }

        jwtUtility.validateJwt(refreshJwt);

        Claims claims = jwtUtility.getClaimsFromAccessJwt(refreshJwt);

        Long userId = Long.valueOf(claims.getSubject());

        User user = userRepository.findById(userId)
                .orElseThrow(InvalidLoginIdException::new);

        String newAccessJwt = jwtUtility.generateAccessJwt(user);

        return new JwtRes(newAccessJwt);
    }
    // 사업자등록번호 조회

    public Object bizStatus(String bizNum) {

        String url = "https://api.odcloud.kr/api/nts-businessman/v1/status"
                + "?serviceKey=" + businessServiceKey
                + "&returnType=JSON";

        // 국세청은 배열 형태로 요청해야 함
        Map<String, Object> body = new HashMap<>();
        body.put("b_no", List.of(bizNum)); // 주의: [] 형태여야 API 정상 동작

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<?> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, entity, Object.class);
    }

    // 카카오 주소 좌표 변환 api
    public KakaoAddressRes searchAddress(String address) {

        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", address)
                        .build())
                .header("Authorization", "KakaoAK " + kakaoRestApiKey)
                .retrieve()
                .bodyToMono(KakaoAddressRes.class)
                .block();
    }
}

