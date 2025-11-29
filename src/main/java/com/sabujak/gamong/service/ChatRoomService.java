package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ChatRoom;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.exception.EmptyItemTradeIdException;
import com.sabujak.gamong.repository.ChatRoomRepository;
import com.sabujak.gamong.repository.ItemTradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ItemTradeRepository itemTradeRepository;

    @Transactional
    public void makeChatRoom(ReqItemTradeId reqItemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(reqItemTradeId.itemTradeId())
                .orElseThrow(EmptyItemTradeIdException::new);

        ChatRoom chatRoom = new ChatRoom(itemTrade);
        chatRoomRepository.save(chatRoom);
    }
}
