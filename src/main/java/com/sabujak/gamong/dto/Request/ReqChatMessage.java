package com.sabujak.gamong.dto.Request;

public record ReqChatMessage(
        Long chatRoomId,
        Long senderUserId,
        String message
) {
}
