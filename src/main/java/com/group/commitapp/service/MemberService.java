package com.group.commitapp.service;


import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.team.createTeamDTO;
import com.group.commitapp.dto.team.invitationTeamDTO;
import com.group.commitapp.repository.MemberRepository;
import com.group.commitapp.repository.TeamRepository;
import com.group.commitapp.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final TeamService teamService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TeamRepository TeamRepository;



    @Transactional
    public Member inviteMember(invitationTeamDTO dto){
        User user = userRepository.findByGithubId(dto.getGithubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
        Optional<Team> team = TeamRepository.findById(dto.getTeamId());
        Member member = new Member(user,team.get(), false);
        if(memberRepository.findByUserAndTeam(user,team.get()).isPresent()){
            throw new IllegalArgumentException("Already invited");
        }
        return memberRepository.save(member);
    }
}
