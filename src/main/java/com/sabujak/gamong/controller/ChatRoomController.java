package com.sabujak.gamong.controller;

import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create/{itemTradeId}")
    public ResponseEntity<String> createChatRoom(@PathVariable Long itemTradeId) {
        chatRoomService.createChatRoom(itemTradeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("채팅룸 생성 성공");
    }
}
