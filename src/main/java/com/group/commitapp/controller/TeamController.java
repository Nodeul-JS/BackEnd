package com.group.commitapp.controller;


import com.group.commitapp.common.dto.ApiResponse;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.domain.Team;
import com.group.commitapp.dto.team.createTeamDTO;
import com.group.commitapp.dto.team.findTeamListDTO;
import com.group.commitapp.dto.team.findMemberListDTO;
import com.group.commitapp.dto.team.invitationTeamDTO;
import com.group.commitapp.service.MemberService;
import com.group.commitapp.service.TeamService;
import com.group.commitapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Team", description = "Team 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/teams")
public class TeamController {
    private final TeamService teamService;
    private final MemberService memberService;
    private final UserService userService;


    @Operation(summary = "내가 가입한 팀 조회 ", description = "사용자가 속한 팀 리스트 정보 반환")
    @GetMapping("/myTeamList/{githubId}")
    public ResponseEntity<ApiResponse<List<findTeamListDTO>>> findGroup(@PathVariable String githubId){
        List<findTeamListDTO> teams = teamService.getTeamsByGitgubId(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(teams, CustomResponseStatus.SUCCESS));
    }



    @Operation(description = "새로운 팀 생성(내가 팀 주인장 됨)", summary = "팀 생성 ")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Team>> createGroup(@RequestBody createTeamDTO dto){
        Team team = teamService.createGroup(dto); // make empty team and save
        return ResponseEntity.ok(ApiResponse.createSuccess(team, CustomResponseStatus.SUCCESS));
    }


    @Operation(description = "팀 초대 (깃허브id로)  " , summary = "그룹 초대")
    @PostMapping("/invitation")
    public ResponseEntity<ApiResponse<Void>> invitationGroup(@RequestBody invitationTeamDTO dto){
        memberService.inviteMember(dto);
        return ResponseEntity.ok(ApiResponse.createSuccess(null, CustomResponseStatus.SUCCESS));
    }

    @Operation(description = "해당 팀의 팀원들 출력" , summary = "해당 팀의 팀원들 출력")
    @GetMapping("/memberList/{teamId}")
    public ResponseEntity<ApiResponse<List<findMemberListDTO>>> findGroupMembers(@PathVariable Long teamId){
        List<findMemberListDTO> DtoList = teamService.getMemberListByTeamId(teamId);
        return ResponseEntity.ok(ApiResponse.createSuccess(DtoList, CustomResponseStatus.SUCCESS));
    }


    @Operation(description = "해당 team id의 그룹 삭제 " , summary = "그룹 삭제")
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(@PathVariable Long teamId){
        teamService.deleteGroup(teamId);
        return ResponseEntity.ok(ApiResponse.createSuccess(null, CustomResponseStatus.SUCCESS));
    }
    @Operation(description = "그룹원 삭제 (아직 개발중) " , summary = "그룹원 삭제(아직 개발중 ㅠ)")
    @DeleteMapping("/delete/{teamId}/{userId}")
    public void deleteGroupMember(@PathVariable Long teamId, @PathVariable Long userId){
        teamService.deleteMember(teamId,userId);
    }

    @Operation(description = "그룹 비활성화", summary = "그룹 비활성화")
    @PutMapping("/disable/{teamId}")
    public ResponseEntity<ApiResponse<Void>> disableGroup(@PathVariable Long teamId){
        teamService.disableGroup(teamId);
        return ResponseEntity.ok(ApiResponse.createSuccess(null, CustomResponseStatus.SUCCESS));
    }




}
