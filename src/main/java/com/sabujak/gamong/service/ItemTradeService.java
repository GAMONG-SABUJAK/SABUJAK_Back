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

    private static final double EARTH_RADIUS_KM = 6371;

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
                itemTrade.getUser().getLatitude(),
                itemTrade.getUser().getLongitude(),
                chatRoomRepository.countChatRoomByItemTradeId(itemTradeId),
                bookmarkRepository.countBookmarkByItemTradeId(itemTradeId)
        );
    }

    // 재고 거래 글 위치별 조회
    public ItemTradeByAddressRes getItemTradeByLocation(double userLat, double userLng) {
        List<ItemTradeRes> allTrades = itemTradeRepository.findAllItemTradeRes();

        // 반경 3km 필터 + 첫 번째 이미지 매핑
        List<ItemTradeRes> filtered = allTrades.stream()
                .filter(trade -> distanceKm(userLat, userLng, trade.latitude(), trade.longitude()) < 3)
                .map(trade -> {
                    FileDTO firstImage = joinItemTradeImageRepository
                            .findFirstByItemTradeIdOrderByIdAsc(trade.itemTradeId())
                            .map(f -> new FileDTO(f.getFileName(), f.getFileType(), f.getFileSize(), f.getFileUrl(), f.getFileKey()))
                            .orElse(null);

                    return new ItemTradeRes(
                            trade.itemTradeId(),
                            firstImage,
                            trade.hashTag(),
                            trade.itemName(),
                            trade.title(),
                            trade.description(),
                            trade.price(),
                            trade.userAddress(),
                            trade.latitude(),
                            trade.longitude(),
                            trade.chatRoomCnt(),
                            trade.bookmarkCnt()
                    );
                })
                .toList();

        return new ItemTradeByAddressRes("사용자 위치 기준", filtered);
    }

    // 두 좌표 간 거리 계산 (Haversine)
    private double distanceKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                        Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
