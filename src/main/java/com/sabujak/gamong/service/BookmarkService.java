package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.Bookmark;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.exception.EmptyItemTradeIdException;
import com.sabujak.gamong.repository.BookmarkRepository;
import com.sabujak.gamong.repository.ItemTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ItemTradeRepository itemTradeRepository;

    // 재고 거래 글 즐겨찾기 토글
    @Transactional
    public String toggleBookmark(User user, ReqItemTradeId reqItemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(reqItemTradeId.itemTradeId())
                .orElseThrow(EmptyItemTradeIdException::new);

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
}
