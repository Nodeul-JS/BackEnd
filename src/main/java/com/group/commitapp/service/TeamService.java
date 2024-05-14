package com.group.commitapp.service;


import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.Users;
import com.group.commitapp.dto.group.createGroupDTO;
import com.group.commitapp.dto.group.findGroupListDTO;
import com.group.commitapp.dto.group.findMemberListDTO;
import com.group.commitapp.repository.MemberRepository;
import com.group.commitapp.repository.TeamRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamService {
private final TeamRepository teamRepository;
 private final MemberRepository memberRepository;
 private final UserRepository userRepository;


 @Transactional
    public List<findGroupListDTO> getTeamsByUserId(Long userId){
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        List<Member> members = memberRepository.findAllByUser(user);
        List<Team> Teams = members.stream().map(Member::getTeam).collect(Collectors.toList());
        return Teams.stream()
                .map(findGroupListDTO::new)// 생성자 참조임 DTO 단에 Team 객체 단일 생성자 필요
                .collect(Collectors.toList());
    }

    @Transactional
    public void createGroup(createGroupDTO dto, Long userId){
     // find group creator(user)
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
    //
        Team team = Team.saveTeam(dto.getGroupName(),dto.getGroupNum(),dto.getDescription());
        teamRepository.save(team); // Team Constructor: Protected -> have to use save method(team.set...)
        Member member = Member.saveMember(user,team);
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
        Users user = userRepository.findById(userId)
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
