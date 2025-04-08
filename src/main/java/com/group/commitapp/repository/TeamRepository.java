package com.group.commitapp.repository;

import com.group.commitapp.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

	//    @Query("SELECT c FROM CommitHistory c WHERE c.user.userId = :userId AND DATE(c.createdAt) =
	// CURRENT_DATE")
	//    List<Team> findTodayCommitsByGithubId(@Param("userId") String githubId);
}
