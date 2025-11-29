package com.sabujak.gamong.domain;

import com.sabujak.gamong.enums.HashTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter // 재고 거래
public class ItemTrade {
    @Id
    @GeneratedValue
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user; // 글 올린 유저

    @Enumerated(EnumType.STRING)
    private HashTag hashTag; // FREE or WANTED or FOR_SALE

    private String itemName; // 재고 이름

    private String title; // 거래 글 제목

    @Lob
    private String description; // 재고 설명

    private long price; // 가격

    private LocalDateTime createAt; // 생성 시점

    private LocalDateTime updateAt; // 수정 시점

    public ItemTrade(User user, HashTag hashTag, String itemName, String title, String description, long price) {
        this.user = user;
        this.hashTag = hashTag;
        this.itemName = itemName;
        this.title = title;
        this.description = description;
        this.price = price;
        this.createAt = LocalDateTime.now();
        this.updateAt = createAt;
    }

    public void changeUpdateAt() {
        this.updateAt = LocalDateTime.now();
    }
}
