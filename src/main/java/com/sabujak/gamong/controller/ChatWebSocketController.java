package com.sabujak.gamong.controller;

import com.sabujak.gamong.domain.ChatMessage;
import com.sabujak.gamong.dto.Request.ReqChatMessage;
import com.sabujak.gamong.dto.Response.ChatMessageRes;
import com.sabujak.gamong.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendChatMessage(@Payload ReqChatMessage reqChatMessage) {

        ChatMessage chatMessage = chatMessageService.sendChatMessage(reqChatMessage);

        ChatMessageRes chatMessageRes = new ChatMessageRes(
                chatMessage.getId(),
                chatMessage.getChatRoomId(),
                chatMessage.getSenderUserId(),
                chatMessage.getMessage(),
                chatMessage.getCreateAt(),
                chatMessage.getReadYn()
        );

        messagingTemplate.convertAndSend(
                "/topic/chat/" + reqChatMessage.chatRoomId(),
                chatMessageRes
        );
    }

}

