package com.group.commitapp.repository;

import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

	//    Optional<Member> findByUserId(Long userId);
	// findAllByUser
	List<Member> findAllByUser(User userId);

	Optional<Member> findById(Long memberId);

	Optional<Member> findByUserAndTeam(User user, Team team);

	boolean existsByUserAndTeam(User user, Team team);
}
