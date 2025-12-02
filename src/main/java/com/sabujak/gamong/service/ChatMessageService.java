package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ChatMessage;
import com.sabujak.gamong.dto.Request.ReqChatMessage;
import com.sabujak.gamong.dto.Response.ChatMessageRes;
import com.sabujak.gamong.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    // 재고 거래 채팅 메시지 보내기
    public ChatMessage sendChatMessage(ReqChatMessage reqChatMessage) {
        ChatMessage chatMessage = new ChatMessage(
                reqChatMessage.chatRoomId(),
                reqChatMessage.senderUserId(),
                reqChatMessage.message()
        );

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    // 재고 거래 채팅 메시지 조회
    public List<ChatMessageRes> getChatMessage(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreateAtAsc(chatRoomId).stream()
                .map(dto -> new ChatMessageRes(
                        dto.getId(),
                        dto.getChatRoomId(),
                        dto.getSenderUserId(),
                        dto.getMessage(),
                        dto.getCreateAt(),
                        dto.getReadYn()
                ))
                .toList();
    }

    // 재고 거래 채팅 메시지 읽기
    @Transactional
    public void readMessage(Long chatRoomId, Long userId) {
        List<ChatMessage> messages = chatMessageRepository
                .findByChatRoomIdAndSenderUserIdNot(chatRoomId, userId);

        messages.forEach(ChatMessage::read);

        chatMessageRepository.saveAll(messages);
    }

}
