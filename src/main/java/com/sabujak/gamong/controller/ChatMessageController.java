package com.sabujak.gamong.controller;

import com.sabujak.gamong.dto.Request.ReqChatMessage;
import com.sabujak.gamong.dto.Response.ChatMessageRes;
import com.sabujak.gamong.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatmessage")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // 재고 거래 채팅 메시지 보내기
    @PostMapping("")
    public ResponseEntity<String> sendChatMessage(@RequestBody ReqChatMessage reqChatMessage) {
        chatMessageService.sendChatMessage(reqChatMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body("메시지 생성 성공");
    }

    // 재고 거래 채팅 메시지 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<ChatMessageRes>> getChatMessage(@PathVariable Long chatRoomId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatMessageService.getChatMessage(chatRoomId));
    }

    // 재고 거래 채팅 메시지 읽기
    @PatchMapping("/read/{chatRoomId}/{userId}")
    public ResponseEntity<String> readMessage(@PathVariable Long chatRoomId, @PathVariable Long userId) {
        chatMessageService.readMessage(chatRoomId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("읽음 처리");
    }

}
