package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.enums.HashTag;

public record ItemTradeRes(
        Long itemTradeId,
        FileDTO itemImage,
        HashTag hashTag,
        String itemName,
        String title,
        String description,
        Long price,
        String userAddress,
        Double latitude,
        Double longitude,
        long chatRoomCnt,
        long bookmarkCnt
) {
}
