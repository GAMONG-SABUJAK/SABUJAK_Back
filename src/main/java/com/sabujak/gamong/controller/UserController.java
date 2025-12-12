package com.sabujak.gamong.controller;

import com.sabujak.gamong.dto.Request.ReqBizStatus;
import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.dto.Response.KakaoAddressRes;
import com.sabujak.gamong.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody ReqSignUp reqSignUp) {
        userService.signUp(reqSignUp);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<JwtRes> signUp(HttpServletResponse response, @RequestBody ReqLogin reqLogin) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(response, reqLogin));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 성공");
    }

    // refreshJwt 재발급
    @PostMapping("/refresh")
    public ResponseEntity<JwtRes> reissuedRefreshJwt(HttpServletRequest request) {
        JwtRes newAccessToken = userService.reissuedRefreshJwt(request);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }

    // 사업자등록번호 조회
    @GetMapping("/biz-status")
    public ResponseEntity<Object> bizStatus(String bizNum) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.bizStatus(bizNum));
    }

    // 카카오 주소 좌표 변환 api
    @GetMapping("/address/search")
    public ResponseEntity<KakaoAddressRes> searchAddress(@RequestParam String address) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.searchAddress(address));
    }

}
