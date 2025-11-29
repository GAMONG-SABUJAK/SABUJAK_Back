package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTrade;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
import com.sabujak.gamong.exception.InvalidUserException;
import com.sabujak.gamong.repository.ItemTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemTradeService {

    private final ItemTradeRepository itemTradeRepository;

    // 재고 거래 글 생성
    @Transactional
    public void createItemTrade(User user , ReqItemTrade reqItemTrade) {
        ItemTrade itemTrade = new ItemTrade(
                user,
                reqItemTrade.hashTag(),
                reqItemTrade.itemName(),
                reqItemTrade.title(),
                reqItemTrade.description(),
                reqItemTrade.price()
        );

        itemTradeRepository.save(itemTrade);
    }

    // 재고 거래 글 삭제
    @Transactional
    public void deleteItemTrade(User user, Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        if (!Objects.equals(user.getId(), itemTrade.getUser().getId()))
            throw new InvalidUserException();

        itemTradeRepository.delete(itemTrade);
    }
}
