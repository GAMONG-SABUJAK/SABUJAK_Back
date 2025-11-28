package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqSignUp;
import com.sabujak.gamong.exception.AlreadyUserIdException;
import com.sabujak.gamong.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public void signUp(ReqSignUp reqSignUp) {
        userRepository.findByUserId(reqSignUp.userId())
                .ifPresent(u -> { throw new AlreadyUserIdException(); });

        User user = new User(reqSignUp.userId(),
                reqSignUp.password(),
                reqSignUp.ceoName(),
                reqSignUp.businessNum(),
                reqSignUp.businessName(),
                reqSignUp.businessType(),
                reqSignUp.businessItem(),
                reqSignUp.businessAddress());

        userRepository.save(user);
    }
}
