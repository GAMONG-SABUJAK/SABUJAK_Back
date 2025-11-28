package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
