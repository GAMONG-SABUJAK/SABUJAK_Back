package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.enums.HashTag;

import java.util.List;

public record ItemTradeByAddressRes(
        String address,
        List<ItemTradeRes> itemTradeResList
) {
}
