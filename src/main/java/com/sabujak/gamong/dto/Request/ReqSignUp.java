package com.sabujak.gamong.dto.Request;

public record ReqSignUp(
        String loginId,
        String password,
        String nickname,
        String ceoName,
        long businessNum,
        String businessName,
        String businessType,
        String businessItem,
        String businessAddress
) {
}
