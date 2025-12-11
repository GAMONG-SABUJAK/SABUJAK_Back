package com.sabujak.gamong.dto.Response;

import com.sabujak.gamong.enums.HashTag;
import lombok.Data;

@Data
public class ItemTradeWithoutFileDtoRes {
    private Long itemTradeId;
    private HashTag hashTag;
    private String itemName;
    private String title;
    private String description;
    private Long price;
    private String userAddress;
    private Double latitude;
    private Double longitude;
    private Long chatRoomCnt;
    private Long bookmarkCnt;

    public ItemTradeWithoutFileDtoRes(Long itemTradeId, HashTag hashTag, String itemName, String title,
                                      String description, Long price, String userAddress,
                                      Double latitude, Double longitude, Long chatRoomCnt, Long bookmarkCnt) {
        this.itemTradeId = itemTradeId;
        this.hashTag = hashTag;
        this.itemName = itemName;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userAddress = userAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chatRoomCnt = chatRoomCnt;
        this.bookmarkCnt = bookmarkCnt;
    }
}
