package com.sabujak.gamong.repository;

import com.sabujak.gamong.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
}
