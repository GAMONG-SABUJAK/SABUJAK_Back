package com.sabujak.gamong.dto.Request;

import com.sabujak.gamong.enums.HashTag;

public record ReqItemTrade(
        HashTag hashTag,
        String itemName,
        String title,
        String description,
        long price
) {
}
