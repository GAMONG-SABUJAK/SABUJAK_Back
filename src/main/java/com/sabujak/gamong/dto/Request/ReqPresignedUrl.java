package com.sabujak.gamong.dto.Request;

public record ReqPresignedUrl(
        String fileName,
        String mimeType
) {
}
