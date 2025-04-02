package com.group.commitapp.repository;

import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CommitHistoryRepository extends JpaRepository<CommitHistory, Long> {
    @Query("SELECT c FROM CommitHistory c WHERE c.user.githubId = :githubId AND DATE(c.createdAt) = CURRENT_DATE")
    List<CommitHistory> findTodayCommitsByGithubId(@Param("githubId") String githubId);
    List<CommitHistory> findByUser(Optional<User> user);
    List<CommitHistory> findByUserOrderByIdAsc(Optional<User> user);
    List<CommitHistory> findByUserOrderByIdAsc(User user);

    List<CommitHistory> findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(User user, Date start, Date end);

}
