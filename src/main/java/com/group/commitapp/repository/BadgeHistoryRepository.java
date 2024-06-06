package com.group.commitapp.repository;

import com.group.commitapp.domain.Badge;
import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeHistoryRepository extends JpaRepository<BadgeHistory, Long> {
    List<BadgeHistory> findAllByUser(User user);
    Optional<BadgeHistory> findByUserAndBadge(User user, Badge badge);

}
