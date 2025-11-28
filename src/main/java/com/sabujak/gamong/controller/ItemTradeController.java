package com.sabujak.gamong.controller;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Request.ReqItemTrade;
import com.sabujak.gamong.service.ItemTradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/itemtrade")
public class ItemTradeController {

    private final ItemTradeService itemTradeService;

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadItemTrade(@AuthenticationPrincipal User user, ReqItemTrade reqItemTrade) {
        itemTradeService.uplodeItemTrade(user, reqItemTrade);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
