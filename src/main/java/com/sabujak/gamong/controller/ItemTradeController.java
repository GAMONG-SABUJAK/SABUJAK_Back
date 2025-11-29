package com.sabujak.gamong.controller;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTrade;
import com.sabujak.gamong.service.ItemTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item-trade")
public class ItemTradeController {

    private final ItemTradeService itemTradeService;

    @PostMapping("/create")
    public ResponseEntity<String> createItemTrade(@AuthenticationPrincipal User user, ReqItemTrade reqItemTrade) {
        itemTradeService.createItemTrade(user, reqItemTrade);
        return ResponseEntity.status(HttpStatus.CREATED).body("재고 거래 글 생성 성공");
    }

    @DeleteMapping("/delete/{itemTradeId}")
    public ResponseEntity<String> deleteItemTrade(@AuthenticationPrincipal User user, @PathVariable Long itemTradeId) {
        itemTradeService.deleteItemTrade(user, itemTradeId);
        return ResponseEntity.status(HttpStatus.OK).body("재고 거래 글 삭제 성공");
    }
}
