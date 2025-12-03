package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.JoinUserImage;
import com.sabujak.gamong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinUserImageRepository extends JpaRepository<JoinUserImage, Long> {
    List<JoinUserImage> findByUser(User user);
}
