package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ChatRoom;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.exception.AlreadyExistChatRoomException;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
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

    // 재고 거래 글 채팅방 생성
    @Transactional
    public void createChatRoom(User user, Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        chatRoomRepository.findByItemTradeIdAndSenderUser(itemTradeId, user)
                .ifPresent(room -> { throw new AlreadyExistChatRoomException(); });

        ChatRoom chatRoom = new ChatRoom(itemTrade);

        chatRoomRepository.save(chatRoom);
    }
}
