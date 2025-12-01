package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ChatMessage;
import com.sabujak.gamong.domain.ChatRoom;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.dto.Request.ReqItemTradeId;
import com.sabujak.gamong.dto.Response.ChatRoomRes;
import com.sabujak.gamong.exception.AlreadyExistChatRoomException;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
import com.sabujak.gamong.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ItemTradeRepository itemTradeRepository;
    private final JoinUserImageRepository joinUserImageRepository;
    private final JoinItemTradeImageRepository joinItemTradeImageRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 재고 거래 글 채팅방 생성
    @Transactional
    public void createChatRoom(User user, Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        chatRoomRepository.findByItemTradeIdAndSenderUser(itemTradeId, user)
                .ifPresent(room -> { throw new AlreadyExistChatRoomException(); });

        ChatRoom chatRoom = new ChatRoom(itemTrade, itemTrade.getUser(), user);

        chatRoomRepository.save(chatRoom);
    }

    // 재고 거래 글 채팅방 조회
    public List<ChatRoomRes> getAllMyChatRoom(User user) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findByReceiverUser(user);

        return chatRoomList.stream()
                .map(this::convertToChatRoomRes)
                .toList();
    }

    private ChatRoomRes convertToChatRoomRes(ChatRoom chatRoom) {
        ChatMessage lastChat = chatMessageRepository
                .findFirstByChatRoomIdOrderByCreateAtDesc(chatRoom.getId());

        return new ChatRoomRes(
                chatRoom.getId(),
                itemTradeConvertToFileDTO(chatRoom.getItemTrade()),
                userConvertToFileDTO(chatRoom.getSenderUser()),
                chatRoom.getSenderUser().getNickname(),
                chatRoom.getSenderUser().getBusinessAddress(),
                chatRoom.getCreateAt(),
                lastChat != null ? lastChat.getMessage() : null
        );
    }

    private FileDTO itemTradeConvertToFileDTO(ItemTrade itemTrade) {
        return joinItemTradeImageRepository.findByItemTrade(itemTrade).stream()
                .map(dto -> new FileDTO(
                        dto.getFileName(),
                        dto.getFileType(),
                        dto.getFileSize(),
                        dto.getFileUrl(),
                        dto.getFileKey()
                ))
                .findFirst()
                .orElse(null);
    }

    private FileDTO userConvertToFileDTO(User user) {
        return joinUserImageRepository.findByUser(user).stream()
                .map(dto -> new FileDTO(
                        dto.getFileName(),
                        dto.getFileType(),
                        dto.getFileSize(),
                        dto.getFileUrl(),
                        dto.getFileKey()
                ))
                .findFirst()
                .orElse(null);
    }


}
