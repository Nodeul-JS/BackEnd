package com.group.commitapp.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/badge")
//@Api(value = "UserController", description = "reward API")
public class BadgeController {
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }
    //@ApiOperation(value = "뱃지 전체 조회 ", notes = "/api/badge/{userId}")
    //@GetMapping("/{userId}")
    public Result<?> findBadge(){
        return new Result<>("findBadgeDTO here");
    }


    //@ApiOperation(value = "뱃지 조회 ", notes = "/api/badge/{badgeId}")
    //@GetMapping("/{badgeId}")
    public Result<?> findBadgeById(){
        return new Result<>("findBadgeByIdDTO here");
    }


    //@ApiOperation(value = "뱃지 지급 ", notes = "/api/create/badge/{userId}/{badgeId} ")
    //@PostMapping("/create/{userId}/{badgeId}")
    public Result<?> createBadge(){
        return new Result<>("createBadgeDTO here");
    }








}
