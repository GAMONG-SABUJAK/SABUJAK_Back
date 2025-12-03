package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.JoinItemTradeImage;
import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.dto.Request.ReqItemTrade;
import com.sabujak.gamong.dto.Response.ItemTradeByAddressRes;
import com.sabujak.gamong.dto.Response.ItemTradeRes;
import com.sabujak.gamong.exception.InvalidItemTradeIdException;
import com.sabujak.gamong.exception.InvalidUserException;
import com.sabujak.gamong.repository.BookmarkRepository;
import com.sabujak.gamong.repository.ChatRoomRepository;
import com.sabujak.gamong.repository.ItemTradeRepository;
import com.sabujak.gamong.repository.JoinItemTradeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemTradeService {

    private final ItemTradeRepository itemTradeRepository;
    private final JoinItemTradeImageService joinItemTradeImageService;
    private final JoinItemTradeImageRepository joinItemTradeImageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Service s3Service;

    // 재고 거래 글 생성
    @Transactional
    public void createItemTrade(User user, ReqItemTrade reqItemTrade) {
        ItemTrade itemTrade = new ItemTrade(
                user,
                reqItemTrade.hashTag(),
                reqItemTrade.itemName(),
                reqItemTrade.title(),
                reqItemTrade.description(),
                reqItemTrade.price()
        );

        itemTradeRepository.save(itemTrade);
        joinItemTradeImageService.createJoinItemTradeImage(itemTrade, reqItemTrade.itemImage());
    }

    // 재고 거래 글 삭제
    @Transactional
    public void deleteItemTrade(User user, Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        if (!Objects.equals(user.getId(), itemTrade.getUser().getId()))
            throw new InvalidUserException();

        // 연관된 S3 파일 키 목록 조회
        List<String> fileKeyList = joinItemTradeImageRepository.findByItemTrade(itemTrade).stream()
                .map(JoinItemTradeImage::getFileKey)
                .filter(key -> key != null && !key.isBlank())
                .toList();

        // S3에서 파일 삭제
        if (!fileKeyList.isEmpty()) {
            s3Service.deleteFiles(fileKeyList);
        }

        itemTradeRepository.delete(itemTrade);
    }

    // 재고 거래 글 개별 조회
    public ItemTradeRes getItemTrade(Long itemTradeId) {
        ItemTrade itemTrade = itemTradeRepository.findById(itemTradeId)
                .orElseThrow(InvalidItemTradeIdException::new);

        return new ItemTradeRes(
                itemTradeId,
                new FileDTO(
                        itemTrade.getJoinItemTradeImageList().getFirst().getFileName(),
                        itemTrade.getJoinItemTradeImageList().getFirst().getFileType(),
                        itemTrade.getJoinItemTradeImageList().getFirst().getFileSize(),
                        itemTrade.getJoinItemTradeImageList().getFirst().getFileUrl(),
                        itemTrade.getJoinItemTradeImageList().getFirst().getFileKey()
                ),
                itemTrade.getHashTag(),
                itemTrade.getItemName(),
                itemTrade.getTitle(),
                itemTrade.getDescription(),
                itemTrade.getPrice(),
                itemTrade.getUser().getBusinessAddress(),
                chatRoomRepository.countChatRoomByItemTradeId(itemTradeId),
                bookmarkRepository.countBookmarkByItemTradeId(itemTradeId)
        );
    }

    // 재고 거래 글 위치별 조회
    public ItemTradeByAddressRes getItemTradeByAddress(String address) {
        List<ItemTradeRes> itemTradeResList = itemTradeRepository.findItemTradeResByUserBusinessAddress(address);

        return new ItemTradeByAddressRes(address, itemTradeResList);
    }
}
