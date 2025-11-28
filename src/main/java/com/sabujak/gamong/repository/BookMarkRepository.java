package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.Bookmark;
import com.sabujak.gamong.domain.ItemTrade;
import com.sabujak.gamong.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndItemTrade(User user, ItemTrade itemTrade);
}
