package com.sabujak.gamong.dto.Response;

import java.time.LocalDateTime;

public record ChatMessageRes(
        String chatMessageId,
        Long chatRoomId,
        Long senderUserId,
        String message,
        LocalDateTime createAt,
        Boolean readYn
) {
}
