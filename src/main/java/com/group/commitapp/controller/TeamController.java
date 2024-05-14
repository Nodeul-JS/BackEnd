package com.group.commitapp.controller;


import com.group.commitapp.dto.group.findGroupListDTO;
import com.group.commitapp.service.TeamService;
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
    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }


    @Operation(summary =  "그룹 조회 ", description = "사용자가 속한 그룹 리스트 정보 반환")
    @GetMapping("/find/{userId}")
    public Result<List<findGroupListDTO>> findGroup(@PathVariable Long userId){
        List<findGroupListDTO> findGroupListDTO = teamService.getTeamsByUserId(userId);
        return new Result<>(findGroupListDTO);
    }

    //@ApiOperation(value = "그룹 생성 - clear", notes = "/api/group")
    @PostMapping("/create")
    public Result<?> createGroup(){
        return new Result<>("createGroupDTO here");
    }

    //@ApiOperation(value = "그룹 초대하기", notes = "/api/group/invitation")
    @PostMapping("/invitation")
    public Result<?> invitationGroup(){
        return new Result<>("invitationGroupDTO here");
    }

    //@ApiOperation(value = "해당 그룹의 그룹원들 출력 ", notes = " api/group/{groupId}")
    @Operation(description = "자세한 설명은 생략" , summary = "해당 그룹의 그룹원들 출력")
    @GetMapping("/find/{groupId}")
    public Result<?> findGroupMembers(@PathVariable Long groupId){
        return new Result<>("findGroupMembersDTO here");
    }


    //@ApiOperation(value = "그룹 삭제", notes = "api/group")
    @Operation(description = "자세한 설명은 생략" , summary = "그룹 삭제")
    @DeleteMapping("/delete/{groupId}")
    public Result<?> deleteGroup(@PathVariable Long groupId){
        return new Result<>("deleteGroupDTO here");
    }



    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }



}
