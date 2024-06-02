package com.group.commitapp.service;


import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.team.createTeamDTO;
import com.group.commitapp.dto.team.findTeamListDTO;
import com.group.commitapp.dto.team.findMemberListDTO;
import com.group.commitapp.repository.MemberRepository;
import com.group.commitapp.repository.TeamRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamService {
private final TeamRepository teamRepository;
 private final MemberRepository memberRepository;
 private final UserRepository userRepository;


 @Transactional
    public List<findTeamListDTO> getTeamsByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        List<Member> members = memberRepository.findAllByUser(user);
        List<Team> Teams = members.stream().map(Member::getTeam).collect(Collectors.toList());
        return Teams.stream()
                .map(findTeamListDTO::new)// 생성자 참조임 DTO 단에 Team 객체 단일 생성자 필요
                .collect(Collectors.toList());
    }

    @Transactional
    public void createGroup(createTeamDTO dto){
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
        Team team = new Team(dto);
        teamRepository.save(team); // Team Constructor: Protected -> have to use save method(team.set...)
        Member member = new Member(user,team, true);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteGroup(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID: There is No ID. Here is Service"));
        teamRepository.delete(team);
    }
    @Transactional
    public void deleteMember(Long teamId, Long userId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID: There is No ID. Here is Service"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        Member member = memberRepository.findByUserAndTeam(user,team)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        memberRepository.delete(member);
    }
    @Transactional
    public void disableGroup(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID: There is No ID. Here is Service"));
        team.setDeleted(true);
    }

    @Transactional
    public List<findMemberListDTO> getMemberListByTeamId(Long teamId) {
        // find Team by teamId
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID: There is No ID. Here is Service"));
        // get members of the team
        //find user info by member constructor
        return team.getMembers().stream()
                .map(findMemberListDTO::new)
                .collect(Collectors.toList());
    }







 }
