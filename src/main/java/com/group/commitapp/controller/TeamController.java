package com.group.commitapp.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/teams")
//@Api(value = "UserController", description = "Team API")
public class TeamController {
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    //@ApiOperation(value = "그룹 조회 ", notes = "/api/group/{userId}")
    @GetMapping("/find/{userId}")
    public Result<?> findGroup(@PathVariable Long userId){
        return new Result<>("findGroupDTO here");
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
    @GetMapping("/find/{groupId}")
    public Result<?> findGroupMembers(@PathVariable Long groupId){
        return new Result<>("findGroupMembersDTO here");
    }


    //@ApiOperation(value = "그룹 삭제", notes = "api/group")
    @DeleteMapping("/delete/{groupId}")
    public Result<?> deleteGroup(@PathVariable Long groupId){
        return new Result<>("deleteGroupDTO here");
    }





}
