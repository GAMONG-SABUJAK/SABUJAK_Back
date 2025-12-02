package com.sabujak.gamong.service;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.JoinItemTradeImage;
import com.sabujak.gamong.dto.FileDTO;
import com.sabujak.gamong.repository.JoinItemTradeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinItemTradeImageService {

    private final JoinItemTradeImageRepository joinItemTradeImageRepository;

    @Transactional
    public void createJoinItemTradeImage(ItemTrade itemTrade, List<FileDTO> fileDTOList) {
        List<JoinItemTradeImage> joinItemTradeImageList = fileDTOList.stream()
                .map(itemImage -> new JoinItemTradeImage(
                        itemTrade,
                        itemImage.fileName(),
                        itemImage.fileType(),
                        itemImage.fileSize(),
                        itemImage.fileUrl(),
                        itemImage.fileKey()
                ))
                .toList();
        joinItemTradeImageRepository.saveAll(joinItemTradeImageList);
    }
}
