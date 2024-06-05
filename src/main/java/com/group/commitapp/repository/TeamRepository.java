package com.group.commitapp.repository;

import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

//    @Query("SELECT c FROM CommitHistory c WHERE c.user.userId = :userId AND DATE(c.createdAt) = CURRENT_DATE")
//    List<Team> findTodayCommitsByGithubId(@Param("userId") String githubId);
}
