package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.*;
import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.dto.Request.ReqChatRoomDetail;
import com.sabujak.gamong.dto.Response.ChatMessageRes;
import com.sabujak.gamong.dto.Response.ChatRoomDetailRes;
import com.sabujak.gamong.dto.Response.ChatRoomRes;
import com.sabujak.gamong.exception.AlreadyExistChatRoomException;
import com.sabujak.gamong.exception.InvalidChatRoomIdException;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
import com.sabujak.gamong.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 내 재고 거래 글 채팅방 리스트 조회
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

    // 재고 거래 채팅방 개별 조회 + 최근 메시지 + 읽음 처리
    @Transactional
    public ChatRoomDetailRes getChatRoomDetail(ReqChatRoomDetail reqChatRoomDetail) {
        ChatRoom chatRoom = chatRoomRepository.findById(reqChatRoomDetail.chatRoomId())
                .orElseThrow(InvalidChatRoomIdException::new);

        ItemTrade itemTrade = chatRoom.getItemTrade();

        List<ChatMessage> messageList = chatMessageRepository
                .findByChatRoomIdOrderByCreateAtAsc(reqChatRoomDetail.chatRoomId());

        long unreadCount = messageList.stream()
                .filter(m -> !m.getSenderUserId().equals(reqChatRoomDetail.receiverUserId()) && !m.getReadYn())
                .count();

        // 읽지 않은 메시지들 읽음 처리
        messageList.stream()
                .filter(m -> !m.getSenderUserId().equals(reqChatRoomDetail.receiverUserId()) && !m.getReadYn())
                .forEach(ChatMessage::read);

        chatMessageRepository.saveAll(messageList);

        String itemUrl = joinItemTradeImageRepository.findTop1ByItemTradeOrderByIdAsc(itemTrade)
                .map(JoinItemTradeImage::getFileUrl)
                .orElse(null);

        List<ChatMessageRes> messageResList = messageList.stream()
                .map(m -> new ChatMessageRes(
                        m.getId(),
                        m.getChatRoomId(),
                        m.getSenderUserId(),
                        m.getMessage(),
                        m.getCreateAt(),
                        m.getReadYn()
                ))
                .toList();

        Long senderUserId = chatRoom.getSenderUser().getId();
        String senderNickname = chatRoom.getSenderUser().getNickname();

        Long receiverUserId = chatRoom.getReceiverUser().getId();

        return new ChatRoomDetailRes(
                reqChatRoomDetail.chatRoomId(),
                itemTrade.getId(),
                itemTrade.getItemName(),
                itemUrl,
                itemTrade.getTitle(),
                itemTrade.getPrice(),
                senderUserId,
                senderNickname,
                receiverUserId,
                messageResList,
                unreadCount
        );
    }
}
