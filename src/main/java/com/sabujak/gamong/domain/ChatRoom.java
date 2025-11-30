package com.sabujak.gamong.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter // 재고 거래 채팅룸
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_trade_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTrade itemTrade; // 재고거래 글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User senderUser; // 메시지 보내려는 사람

    private LocalDateTime createAt; // 생성 시점

    public ChatRoom(ItemTrade itemTrade) {
        this.itemTrade = itemTrade;
        this.createAt = LocalDateTime.now();
    }
}
