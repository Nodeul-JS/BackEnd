package com.group.commitapp.repository;

import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeHistoryRepository extends JpaRepository<BadgeHistory, Long> {
    List<BadgeHistory> findAllByUser(Users user);


}
