package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqLogin;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.dto.Response.JwtRes;
import com.sabujak.gamong.exception.AlreadyLoginIdException;
import com.sabujak.gamong.exception.InvalidPasswordException;
import com.sabujak.gamong.exception.InvalidLoginIdException;
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
        userRepository.findByLoginId(reqSignUp.loginId())
                .ifPresent(u -> { throw new AlreadyLoginIdException(); });

        User user = new User(
                reqSignUp.loginId(),
                reqSignUp.password(),
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
    public JwtRes login(ReqLogin reqLogin) {
        User user = userRepository.findByLoginId(reqLogin.loginId())
                .orElseThrow(InvalidLoginIdException::new);

        if (!user.checkPassword(reqLogin.password()))
            throw new InvalidPasswordException();

        return new JwtRes(jwtUtility.generateJwt(user.getLoginId()));
    }
}
