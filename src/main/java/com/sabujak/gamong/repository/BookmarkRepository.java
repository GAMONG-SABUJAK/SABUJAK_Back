package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.Bookmark;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Response.ItemTradeRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndItemTrade(User user, ItemTrade itemTrade);

    long countBookmarkByItemTradeId(Long itemTradeId);

    List<Bookmark> findByUser(User user);

    @Query("""
    SELECT i
    FROM Bookmark b
    JOIN b.itemTrade i
    JOIN i.user u
    LEFT JOIN FETCH i.chatRoomList
    LEFT JOIN FETCH i.bookmarkList
    LEFT JOIN FETCH i.joinItemTradeImageList
    WHERE b.user = :user
""")
    List<ItemTrade> findBookmarkedItemTradesByUserEntity(@Param("user") User user);

}
