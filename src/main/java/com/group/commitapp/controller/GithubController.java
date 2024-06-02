package com.group.commitapp.controller;


import com.group.commitapp.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/github")
@Tag(name = "Github", description = "Github 관련 API")
@AllArgsConstructor
public class GithubController {
    private final GitHubService gitHubService;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


//    @GetMapping("/{userId}")
//    public Result<?> findCommitList(){
//        return new Result<>("findCommitListDTO here");
//    }
    @GetMapping("/todayCommit/{githubId}")
    @Operation(summary =  "오늘 깃허브 커밋 이력", description = "user의 오늘 깃허브 커밋 이력 조회, 없으면 null, 500에러 반환")
    public ResponseEntity<List<String>> getTodayCommitUrls(@PathVariable String githubId) {
        try {
            List<String> commitUrls = gitHubService.getTodayCommitUrls(githubId);
            System.out.println(commitUrls);
            return ResponseEntity.ok(commitUrls);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
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
