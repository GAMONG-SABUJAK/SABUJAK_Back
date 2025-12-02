package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.Bookmark;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.dto.Response.ItemTradeRes;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
import com.sabujak.gamong.repository.BookmarkRepository;
import com.sabujak.gamong.repository.ChatRoomRepository;
import com.sabujak.gamong.repository.ItemTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ItemTradeRepository itemTradeRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 재고 거래 글 즐겨찾기 토글
    @Transactional
    public String toggleBookmark(User user, Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        return bookmarkRepository.findByUserAndItemTrade(user, itemTrade)
                .map(bookmark -> {
                    bookmarkRepository.delete(bookmark);
                    return "취소되었습니다";
                })
                .orElseGet(() -> {
                    bookmarkRepository.save(new Bookmark(user, itemTrade));
                    return "눌렀습니다";
                });
    }

    // 내 즐겨찾기 리스트 보기
    public List<ItemTradeRes> getMyBookmarkList(User user) {
        return bookmarkRepository.findBookmarkedItemTradesByUser(user);
    }
}
