package com.sabujak.gamong.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class User {

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

    private LocalDateTime createAt; // 생성시점

    public User(String userId, String password, String ceoName, long businessNum, String businessName, String businessType, String businessItem, String businessAddress) {
        this.userId = userId;
        this.password = password;
        this.ceoName = ceoName;
        this.businessNum = businessNum;
        this.businessName = businessName;
        this.businessType = businessType;
        this.businessItem = businessItem;
        this.businessAddress = businessAddress;
        this.createAt = LocalDateTime.now();
    }
}
