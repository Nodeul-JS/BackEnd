package com.group.commitapp.service;

import com.group.commitapp.domain.Member;
import com.group.commitapp.domain.Team;
import com.group.commitapp.dto.request.team.CreateTeamRequest;
import com.group.commitapp.repository.MemberRepository;
import com.group.commitapp.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Team createTeam(CreateTeamRequest request) {
        // 팀 생성
        Team team = new Team(request);
//        team.setMembers(new ArrayList<>());
        // 리더 설정
        Member leader = memberRepository.findById(request.getLeaderId()).orElseThrow(() -> new RuntimeException("Leader not found"));
        leader.setTeam(team);
        leader.setIsHead(true);;

        team.getMembers().add(leader);

        // 팀 저장
        return teamRepository.save(team);
    }
}
