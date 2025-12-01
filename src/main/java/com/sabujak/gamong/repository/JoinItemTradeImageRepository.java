package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.JoinItemTradeImage;
import com.sabujak.gamong.domain.JoinUserImage;
import com.sabujak.gamong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JoinItemTradeImageRepository extends JpaRepository<JoinItemTradeImage, Long> {
    List<JoinItemTradeImage> findByItemTrade(ItemTrade itemTrade);
}
