package com.group.commitapp.service;


import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.common.exception.CustomException;
import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.team.*;
import com.group.commitapp.repository.MemberRepository;
import com.group.commitapp.repository.TeamRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeamService {
private final TeamRepository teamRepository;
 private final MemberRepository memberRepository;
 private final UserRepository userRepository;
 private final BadgeService badgeService;

    @Transactional
    public List<TeamSearchResponse> getTeamsByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        List<Member> members = memberRepository.findAllByUser(user);
        List<Team> Teams = members.stream().map(Member::getTeam).toList();
        return Teams.stream()
                .map(TeamSearchResponse::new)// 생성자 참조임 DTO 단에 Team 객체 단일 생성자 필요
                .collect(Collectors.toList());
    }


// @Transactional
//    public List<findTeamListDTO> getTeamsByUserId(Long userId){
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
//        List<Member> members = memberRepository.findAllByUser(user);
//        List<Team> Teams = members.stream().map(Member::getTeam).toList();
//        return Teams.stream()
//                .map(findTeamListDTO::new)// 생성자 참조임 DTO 단에 Team 객체 단일 생성자 필요
//                .collect(Collectors.toList());
//    }
    @Transactional
    public List<TeamSearchResponse> getTeamsByGithubId(String githubId){
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
        return user.getMembers()
                                    .stream()
                                    .sorted(Comparator.comparing(Member::getId))
                                    .map(Member::getTeam)
                                    .map(TeamSearchResponse::new)
                                    .toList();
    }

    @Transactional
    public void createGroup(TeamCreateRequest dto){
        User user = userRepository.findByGithubId(dto.getGithubId())
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
        Team team = Team.createWithLeader(dto.getTeamName(), dto.getMaxMember(), dto.getDescription(), user);
        teamRepository.save(team); // Team Constructor: Protected -> have to use save method(team.set...)

        badgeService.createLeaderBadge(user , 1L); // 그룹 리더 뱃지 Id = 1
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
        team.deleteTeam();
    }

    @Transactional
    public List<findMemberListDTO> getMemberListByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.TEAM_NOT_FOUND));
        List<findMemberListDTO> members = new ArrayList<>();

        team.getMembers()
                .stream().sorted( Comparator.comparing(Member::getId))
                .forEach(member -> members.add(new findMemberListDTO(member)));
        return members;
    }




    @Transactional
    public MemberInviteResponse inviteMember(MemberInviteRequest dto){
        User user = userRepository.findByGithubId(dto.getGithubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID!! : There is No ID. Here is Service"));

        if(memberRepository.existsByUserAndTeam(user, team)){
            throw new IllegalArgumentException("Already invited");
        }

        Member member = Member.createAsMember(user,team);
        memberRepository.save(member);
        return MemberInviteResponse.from(member);
    }


 }
