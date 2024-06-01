package com.group.commitapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/git")
@Tag(name = "Git", description = "Git 관련 API")
public class GitController {
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @GetMapping("/{userId}")
    public Result<?> findCommitList(){
        return new Result<>("findCommitListDTO here");
    }



    @PutMapping("/update/Streak/{userId}")
    @Operation(summary =  "Streak 지급 ", description = "user Streak update")
    public Result<?> updateStreak(){
        return new Result<>("updateStreakDTO here");
    }



    //@ApiOperation(value = "스트릭 지급 ", notes = "/api/git/update/freeze/{userId}")
    @PutMapping("/update/freeze/{userId}")
    @Operation(summary =  "프리즈 지급 ", description = "user 프리즈 update")
    public Result<?> updateFreeze(){
        return new Result<>("updateFreezeDTO here");



    }






}
