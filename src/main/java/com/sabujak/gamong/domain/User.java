package com.sabujak.gamong.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@NoArgsConstructor
@Getter // 유저
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id; // pk

    private String userId; // 아이디

    private String password; // 비밀번호

    private String ceoName; // 대표자명

    private long businessNum; // 사업자등록번호

    private String businessName; // 상호명

    private String businessType; // 주업태명

    private String businessItem; // 주종목

    private String businessAddress; // 사업장주소

    private LocalDateTime createAt; // 생성 시점

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public User(String userId, String password, String ceoName, long businessNum, String businessName, String businessType, String businessItem, String businessAddress) {
        this.userId = userId;
        this.setPassword(password);
        this.ceoName = ceoName;
        this.businessNum = businessNum;
        this.businessName = businessName;
        this.businessType = businessType;
        this.businessItem = businessItem;
        this.businessAddress = businessAddress;
        this.createAt = LocalDateTime.now();
    }

    public void setPassword(String password) {
        this.password = passwordEncoding(password);
    }

    public String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean checkPassword(String rawPassword) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    // UserDetails 필수 impl
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
