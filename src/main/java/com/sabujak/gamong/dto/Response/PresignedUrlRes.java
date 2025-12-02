package com.sabujak.gamong.dto.Response;

public record PresignedUrlRes(
        String presignedUrl,
        String cdnUrl,
        String key
) {
}
