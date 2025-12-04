package com.sabujak.gamong.dto.Request;

import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.enums.HashTag;

import java.util.List;

public record ReqItemTrade(
        List<FileDTO> itemImage,
        HashTag hashTag,
        String itemName,
        String title,
        String description,
        Long price
) {
}
