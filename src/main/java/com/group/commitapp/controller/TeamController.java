package com.group.commitapp.controller;


import com.group.commitapp.dto.group.createGroupDTO;
import com.group.commitapp.dto.group.findGroupListDTO;
import com.group.commitapp.dto.group.findMemberListDTO;
import com.group.commitapp.service.MemberService;
import com.group.commitapp.service.TeamService;
import com.group.commitapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/teams")
@Tag(name = "Team", description = "Team 관련 API")
public class TeamController {
    private final TeamService teamService;
    private final MemberService memberService;
    private final UserService userService;
    @Autowired
    public TeamController(TeamService teamService, MemberService memberService, UserService userService){
        this.teamService = teamService;
        this.memberService = memberService;
        this.userService = userService;
    }


    @Operation(summary =  "그룹 조회 ", description = "사용자가 속한 그룹 리스트 정보 반환")
    @GetMapping("/find/{userId}")
    public Result<List<findGroupListDTO>> findGroup(@PathVariable Long userId){
        List<findGroupListDTO> findGroupListDTO = teamService.getTeamsByUserId(userId);
        return new Result<>(findGroupListDTO);
    }


    @PostMapping("/create/{userId}")
    public void createGroup(@RequestBody createGroupDTO dto ,@PathVariable Long userId){
        teamService.createGroup(dto,userId); // make empty team and save
        //memberService.createGroupAndMember(dto,userId);// add user as member
        // POST : 저장 할거니까 더이상 쓸게 없다
    }



    @PostMapping("/invitation")
    public Result<?> invitationGroup(){
        return new Result<>("invitationGroupDTO here");
    }

    //@ApiOperation(value = "해당 그룹의 그룹원들 출력 ", notes = " api/group/{groupId}")
    @Operation(description = "자세한 설명은 생략" , summary = "해당 그룹의 그룹원들 출력")
    @GetMapping("/find/{teamId}")
    public Result<List<findMemberListDTO>> findGroupMembers(@PathVariable Long teamId){
        List<findMemberListDTO> DtoList = teamService.getMemberListByTeamId(teamId);
        return new Result<>(DtoList);
        //
    }


    //@ApiOperation(value = "그룹 삭제", notes = "api/group")
    @Operation(description = "그룹 삭제 " , summary = "그룹 삭제")
    @DeleteMapping("/delete/{teamId}")
    public void deleteGroup(@PathVariable Long teamId){
        teamService.deleteGroup(teamId);

    }
    @Operation(description = "그룹원 삭제 " , summary = "그룹원 삭제")
    @DeleteMapping("/delete/{teamId}/{userId}")
    public void deleteGroupMember(@PathVariable Long teamId, @PathVariable Long userId){
        teamService.deleteMember(teamId,userId);
    }

    @Operation(description = "그룹 비활성화", summary = "그룹 비활성화")
    @PutMapping("/disable/{teamId}")
    public void disableGroup(@PathVariable Long teamId){
        teamService.disableGroup(teamId);
    }



    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}
