package com.sabujak.gamong.dto.Response;

import java.util.List;

public record ChatRoomDetailRes(
        Long chatRoomId,
        Long itemTradeId,
        String itemName,
        String itemImageUrl,
        String title,
        Long price,

        Long senderUserId,
        String senderNickname,

        Long receiverUserId,

        Long unreadCount,
        List<ChatMessageRes> messageList
) {}

