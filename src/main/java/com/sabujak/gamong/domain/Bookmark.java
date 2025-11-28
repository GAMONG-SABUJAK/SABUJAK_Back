package com.sabujak.gamong.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter // 재고 즐겨찾기
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user; // 즐겨찾기 한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_trade_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTrade itemTrade; // 즐겨찾기 된 거래글

    private LocalDateTime createAt; // 생성시점

    public Bookmark(User user, ItemTrade itemTrade) {
        this.user = user;
        this.itemTrade = itemTrade;
        this.createAt = LocalDateTime.now();enum
    }
}
