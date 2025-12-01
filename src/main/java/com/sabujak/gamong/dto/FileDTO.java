package com.sabujak.gamong.dto;

import com.sabujak.gamong.enums.AllowedFileType;

public record FileDTO(
        String fileName,
        AllowedFileType fileType,
        Long fileSize,
        String fileUrl,
        String fileKey
) {
}
