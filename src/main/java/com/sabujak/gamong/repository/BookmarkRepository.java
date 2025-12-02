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
        SELECT new com.sabujak.gamong.dto.Response.ItemTradeRes(
            i.id,
            new com.sabujak.gamong.dto.FileDTO(
                                f.fileName,
                                f.fileType,
                                f.fileSize,
                                f.fileUrl,
                                f.fileKey
                            ),
            i.hashTag,
            i.itemName,
            i.title,
            i.description,
            i.price,
            u.businessAddress,
            COUNT(DISTINCT c.id),
            COUNT(DISTINCT b2.id)
        )
        FROM Bookmark b
        JOIN b.itemTrade i
        JOIN i.user u
        LEFT JOIN i.chatRoomList c
        LEFT JOIN i.bookmarkList b2
        LEFT JOIN i.joinItemTradeImageList f
        WHERE b.user = :user
        GROUP BY i.id, i.hashTag, i.itemName, i.title, i.description, i.price, u.businessAddress
    """)
    List<ItemTradeRes> findBookmarkedItemTradesByUser(@Param("user") User user);
}
