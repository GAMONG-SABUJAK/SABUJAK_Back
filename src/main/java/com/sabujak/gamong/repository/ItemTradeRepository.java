package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.dto.Response.ItemTradeRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemTradeRepository extends JpaRepository<ItemTrade, Long> {
    List<ItemTrade> findByUser_BusinessAddress(String userBusinessAddress);

    @Query("""
        SELECT new com.sabujak.gamong.dto.Response.ItemTradeRes(
            i.id,
            NULL,
            i.hashTag,
            i.itemName,
            i.title,
            i.description,
            i.price,
            u.businessAddress,
            u.latitude,
            u.longitude,
            (SELECT COUNT(c.id) FROM i.chatRoomList c),
            (SELECT COUNT(b.id) FROM i.bookmarkList b)
        )
        FROM ItemTrade i
        JOIN i.user u
    """)
    List<ItemTradeRes> findAllItemTradeRes();


}
