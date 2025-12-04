package com.sabujak.gamong.dto.Response;

import java.util.List;

public record ChatRoomDetailRes(
        Long chatRoomId,
        Long itemTradeId,
        String itemName,
        String itemImageUrl,
        String title,
        String price,

        Long senderUserId,
        String senderNickname,

        Long receiverUserId,

        List<ChatMessageRes> messageList,
        Long unreadCount
) {}

