package com.sabujak.gamong.controller;

import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(ReqSignUp reqSignUp) {
        userService.signUp(reqSignUp);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtRes> signUp(ReqLogin reqLogin) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(reqLogin));
    }

}
