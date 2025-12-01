package com.sabujak.gamong.controller;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.dto.Response.ChatRoomRes;
import com.sabujak.gamong.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/create/{itemTradeId}")
    public ResponseEntity<String> createChatRoom(@AuthenticationPrincipal User user, @PathVariable Long itemTradeId) {
        chatRoomService.createChatRoom(user, itemTradeId);
        return ResponseEntity.status(HttpStatus.CREATED).body("채팅룸 생성 성공");
    }

    @GetMapping("/all-my")
    public ResponseEntity<List<ChatRoomRes>> getAllMyChatRoom(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(chatRoomService.getAllMyChatRoom(user));
    }
}
