package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdOrderByCreateAtAsc(Long chatRoomId);
    ChatMessage findFirstByChatRoomIdOrderByCreateAtDesc(Long chatRoomId);

    List<ChatMessage> findByChatRoomIdAndSenderUserIdNot(Long chatRoomId, Long senderUserId);

}
