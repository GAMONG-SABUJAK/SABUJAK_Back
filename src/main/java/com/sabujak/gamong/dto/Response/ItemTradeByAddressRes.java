package com.sabujak.gamong.dto.Response;

import java.util.List;

public record ItemTradeByAddressRes(
        String address,
        List<ItemTradeRes> itemTradeResList
) {
}
