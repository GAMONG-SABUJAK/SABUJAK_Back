package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.Bookmark;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.exception.EmptyItemTradeIdException;
import com.sabujak.gamong.repository.BookMarkRepository;
import com.sabujak.gamong.repository.ItemTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final ItemTradeRepository itemTradeRepository;

    @Transactional
    public String toggleBookmark(User user, ReqItemTradeId reqItemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(reqItemTradeId.itemTradeId())
                .orElseThrow(EmptyItemTradeIdException::new);

        return bookMarkRepository.findByUserAndItemTrade(user, itemTrade)
                .map(bookmark -> {
                    bookMarkRepository.delete(bookmark);
                    return "취소되었습니다";
                })
                .orElseGet(() -> {
                    bookMarkRepository.save(new Bookmark(user, itemTrade));
                    return "눌렀습니다";
                });
    }
}
