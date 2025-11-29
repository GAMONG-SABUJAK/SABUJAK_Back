package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ItemTrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemTradeRepository extends JpaRepository<ItemTrade, Long> {
    List<ItemTrade> findByUser_BusinessAddress(String userBusinessAddress);
}
