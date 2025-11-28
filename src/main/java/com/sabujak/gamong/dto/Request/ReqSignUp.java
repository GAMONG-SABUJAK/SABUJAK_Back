package com.sabujak.gamong.dto.Request;

public record ReqSignUp(
        String userId,
        String password,
        String ceoName,
        long businessNum,
        String businessName,
        String businessType,
        String businessItem,
        String businessAddress
) {
}
