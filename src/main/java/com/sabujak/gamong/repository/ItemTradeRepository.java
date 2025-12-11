package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.dto.Response.ItemTradeWithoutFileDtoRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemTradeRepository extends JpaRepository<ItemTrade, Long> {
    List<ItemTrade> findByUser_BusinessAddress(String userBusinessAddress);
}
