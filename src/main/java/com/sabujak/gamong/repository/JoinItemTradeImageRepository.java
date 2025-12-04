package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.JoinItemTradeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinItemTradeImageRepository extends JpaRepository<JoinItemTradeImage, Long> {
    List<JoinItemTradeImage> findByItemTrade(ItemTrade itemTrade);

    Optional<JoinItemTradeImage> findTop1ByItemTradeOrderByIdAsc(ItemTrade itemTrade);
}
