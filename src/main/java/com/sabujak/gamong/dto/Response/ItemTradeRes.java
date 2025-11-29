package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.enums.HashTag;

public record ItemTradeRes(
        Long itemTradeId,
        HashTag hashTag,
        String title,
        String description,
        long price,
        String userAddress,
        long chatRoomCnt,
        long bookmarkCnt
) {
}
