package com.group.commitapp.controller;


import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.dto.commit.CommitHistoryDTO;
import com.group.commitapp.dto.commit.CommitReviewDTO;
import com.group.commitapp.service.CommitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/commit")
@Tag(name = "Github Commit", description = "Github Commit 관련 API")
@AllArgsConstructor
public class CommitController {
    private final CommitService commitService;
    private CommitService commitHistoryService;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    @GetMapping("/commitStatus/{githubId}")
    @Operation(summary =  "오늘 깃허브 커밋 이력 (3가지 상태)", description = "user의 오늘 깃허브 커밋 이력 조회 {commitNotYet, AINotYet, commitDone} 없으면 null, 500에러 반환")
    public ResponseEntity<String> getWhetherCommitToday(@PathVariable String githubId) {
        try {
//            List<String> commitUrls = gitHubService.getTodayCommitUrls(githubId);
            if(commitService.getTodayCommitUrls(githubId).isEmpty()){
                return ResponseEntity.ok("commitNotYet");
            }
//            List<CommitHistory> commitHistories = commitHistoryService.getTodayCommitsByGithubId(githubId);
            if(commitHistoryService.getTodayCommitsByGithubId(githubId).isEmpty()){
                return ResponseEntity.ok("AINotYet");
            }

            return ResponseEntity.ok("commitDone");
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/todayCommit/{githubId}")
    @Operation(summary =  "오늘자 깃허브 커밋 url리스트 반환(디버깅용임)", description = "user의 오늘 깃허브 커밋 이력 조회, 없으면 null, 아니면 500에러 반환")
    public ResponseEntity<List<String>> getTodayCommitUrls(@PathVariable String githubId) {
        try {
            List<String> commitUrls = commitService.getTodayCommitUrls(githubId);
//            System.out.println(commitUrls);
            return ResponseEntity.ok(commitUrls);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/todayCommit/{githubId}")
    @Operation(summary =  "백엔드DB에 커밋 이력 생성", description = "user의 커밋 이력 생성 - 오늘 깃허브 커밋 이력이 있어야함")
    public ResponseEntity<CommitHistory> addReviewToCommit(@PathVariable String githubId) throws IOException {

        CommitHistory updatedCommitHistory = commitService.addReviewToCommit(githubId);
        return ResponseEntity.ok(updatedCommitHistory);
    }

    @GetMapping("/commitHistory/{githubId}")
    @Operation(summary =  "역대 모든 커밋히스토리(백엔드DB에 있는거) 조회", description = "user의 모든 커밋 이력 조회")
    public List<CommitHistory> getTodayCommits(@PathVariable String githubId) {
        return commitHistoryService.getCommitsByGithubId(githubId);
    }
    /*
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
*/

}
