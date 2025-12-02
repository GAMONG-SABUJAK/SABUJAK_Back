package com.sabujak.gamong.dto.Response;

public record PresignedUrlRes(
        String uploadUrl,
        String cdnUrl,
        String key
) {
}
