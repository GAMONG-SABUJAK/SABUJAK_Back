package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ChatRoom;
import com.sabujak.gamong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    long countChatRoomByItemTradeId(Long itemTradeId);

    Optional<ChatRoom> findByItemTradeIdAndSenderUser(Long itemTradeId, User senderUser);
}
