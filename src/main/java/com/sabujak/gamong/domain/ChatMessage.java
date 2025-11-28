package com.sabujak.gamong.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Document(collection = "chat_messages") // 채팅 메시지
public class ChatMessage {
    @Id
    private String id; // pk

    private Long chatRoomId; // 해당 거래글

    private Long senderUserId; // 메시지 보낸 사람

    private String message; // 메시지 내용

    private LocalDateTime createAt; // 생성 시점

    private Boolean readYn; // 읽음 여부

    public ChatMessage(Long chatRoomId, Long senderUserId, String message) {
        this.chatRoomId = chatRoomId;
        this.senderUserId = senderUserId;
        this.message = message;
        this.createAt = LocalDateTime.now();
        this.readYn = false;
    }
}
