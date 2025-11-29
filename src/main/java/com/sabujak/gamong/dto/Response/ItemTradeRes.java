package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.enums.HashTag;

public record ItemTradeRes(
        Long itemTradeId,
        HashTag hashTag,
        String itemName,
        String title,
        String description,
        String price,
        String userAddress,
        long chatRoomCnt,
        long bookmarkCnt
) {
}
