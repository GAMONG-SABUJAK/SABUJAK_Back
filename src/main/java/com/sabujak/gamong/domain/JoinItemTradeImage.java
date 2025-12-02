package com.sabujak.gamong.domain;

import com.sabujak.gamong.enums.AllowedFileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity // 재고 거래 이미지
public class JoinItemTradeImage {

    @Id
    @GeneratedValue
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_trade_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ItemTrade itemTrade;

    private String fileName; // 파일 이름

    @Enumerated(EnumType.STRING)
    private AllowedFileType fileType; // 파일 타입

    private Long fileSize; // 파일 사이즈

    private String fileUrl; // CDN URL

    private String fileKey; // 파일 저장된 경로

    private LocalDateTime createDate; // YYYY-MM-DD HH:MM:SS.nnnnnn // 파일 생성일

    private LocalDateTime updateDate; // YYYY-MM-DD HH:MM:SS.nnnnnn // 파일 수정일

    private Boolean isUpdate; // 파일 수정 여부 // true or false

    public JoinItemTradeImage(ItemTrade itemTrade, String fileName, AllowedFileType fileType, Long fileSize, String fileUrl, String fileKey) {
        this.itemTrade = itemTrade;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
        this.createDate = LocalDateTime.now();
        this.isUpdate = false;
    }
}
