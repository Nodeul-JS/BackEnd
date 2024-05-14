package com.group.commitapp.repository;

import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(Long userId);
    //findAllByUser
    List<Member> findAllByUser(Users userId);
    Optional<Member> findById(Long memberId);
    Optional<Member>findByUserAndTeam(Users user, Team team);


}
