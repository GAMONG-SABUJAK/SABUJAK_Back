package com.sabujak.gamong.controller;

import com.sabujak.gamong.domain.User;
import com.sabujak.gamong.dto.Response.ItemTradeRes;
import com.sabujak.gamong.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/toggle/{itemTradeId}")
    public ResponseEntity<String> toggleBookmark(@AuthenticationPrincipal User user, @PathVariable Long itemTradeId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookmarkService.toggleBookmark(user, itemTradeId));
    }

    @GetMapping("/my-list")
    public ResponseEntity<List<ItemTradeRes>> getMyBookmarkList(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.OK).body(bookmarkService.getMyBookmarkList(user));
    }
}
