package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.enums.AllowedFileType;

import java.time.LocalDateTime;

public record ChatRoomRes(
        Long chatRoomId,
        FileDTO itemImage,
        FileDTO userImage,
        String senderNickname,
        String senderAddress,
        LocalDateTime chatRoomCreateAt,
        String lastMessage
) {
}
