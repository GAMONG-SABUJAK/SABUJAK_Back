package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdOrderByCreateAtAsc(Long chatRoomId);

    List<ChatMessage> findByChatRoomIdAndSenderUserIdNot(Long chatRoomId, Long senderUserId);

}
