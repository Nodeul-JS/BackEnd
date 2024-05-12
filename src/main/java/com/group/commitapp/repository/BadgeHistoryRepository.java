package com.group.commitapp.repository;

import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeHistoryRepository extends JpaRepository<BadgeHistory, Long> {
}
