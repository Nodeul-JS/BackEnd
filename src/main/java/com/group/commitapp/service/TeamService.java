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

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService {
private final TeamRepository teamRepository;
private final MemberRepository memberRepository;
private final UserRepository userRepository;
private final BadgeService badgeService;

    @Transactional(readOnly = true)
    public List<TeamInfoResponse> getTeamsByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
        List<Member> members = memberRepository.findAllByUser(user);
        List<Team> Teams = members.stream().map(Member::getTeam).toList();
        return TeamInfoResponse.fromList(Teams);
    }


    private User getUserByGithubId(String githubId) {
        return userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
    }


    @Transactional(readOnly = true)
    public List<TeamInfoResponse> getTeamsByGithubId(String githubId){
        User user = getUserByGithubId(githubId);
        return TeamInfoResponse.fromList(user.getTeams());
    }

    @Transactional
    public void createGroup(TeamCreateRequest dto){
        User user = getUserByGithubId(dto.getGithubId());
        Team team = Team.createWithLeader(dto.getTeamName(), dto.getMaxMember(), dto.getDescription(), user);
        teamRepository.save(team); // Team Constructor: Protected -> have to use save method(team.set...)

        badgeService.createLeaderBadge(user); // 리더 뱃지 지급
    }

    @Transactional
    public void deleteGroup(Long teamId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.TEAM_NOT_FOUND));
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
    public List<MemberInfoResponse> getMemberListByTeamId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.TEAM_NOT_FOUND));
        return MemberInfoResponse.fromList(team.getMembers());
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
