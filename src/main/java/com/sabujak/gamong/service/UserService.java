package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.exception.AlreadyUserIdException;
import com.sabujak.gamong.exception.InvalidPasswordException;
import com.sabujak.gamong.exception.InvalidUserIdException;
import com.sabujak.gamong.repository.UserRepository;
import com.sabujak.gamong.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtility jwtUtility;

    // 회원가입
    @Transactional
    public void signUp(ReqSignUp reqSignUp) {
        userRepository.findByUserId(reqSignUp.userId())
                .ifPresent(u -> { throw new AlreadyUserIdException(); });

        User user = new User(
                reqSignUp.userId(),
                reqSignUp.password(),
                reqSignUp.ceoName(),
                reqSignUp.businessNum(),
                reqSignUp.businessName(),
                reqSignUp.businessType(),
                reqSignUp.businessItem(),
                reqSignUp.businessAddress());

        userRepository.save(user);
    }

    // 로그인
    public JwtRes login(ReqLogin reqLogin) {
        User user = userRepository.findByUserId(reqLogin.userId())
                .orElseThrow(InvalidUserIdException::new);
        if (!user.checkPassword(reqLogin.password()))
            throw new InvalidPasswordException();
        return new JwtRes(jwtUtility.generateJwt(user.getUserId()));
    }
}
