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
            i.hashTag,
            i.itemName,
            i.title,
            i.description,
            i.price,
            u.businessAddress,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT b.id)
        )
        FROM ItemTrade i
        JOIN i.user u
        LEFT JOIN i.chatRoomList c
        LEFT JOIN i.bookmarkList b
        WHERE u.businessAddress = :address
        GROUP BY i.id, i.hashTag, i.itemName, i.title, i.description, i.price, u.businessAddress
    """)
    List<ItemTradeRes> findItemTradeResByUserBusinessAddress(@Param("address") String address);
}
