package com.group.commitapp.controller;


import com.group.commitapp.common.dto.ApiResponse;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.dto.commit.CommitHistoryDTO;
import com.group.commitapp.dto.commit.CommitReviewDTO;
import com.group.commitapp.service.CommitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Github Commit", description = "Github Commit 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/commit")
public class CommitController {
    private final CommitService commitService;


    @GetMapping("/commitStatus/{githubId}")
    @Operation(summary =  "오늘 깃허브 커밋 상태 조회", description = "commitNotYet, AINotYet, commitDone 중 하나 반환")
    public ResponseEntity<ApiResponse<String>> getCommitStatus(@PathVariable String githubId) throws IOException {
        String status = commitService.getCommitStatusForToday(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(status, CustomResponseStatus.SUCCESS));
    }


    //테스트 디버깅용
    @GetMapping("/todayCommit/{githubId}")
    @Operation(summary =  "오늘자 깃허브 커밋 url리스트 반환(디버깅용임)", description = "user의 오늘 깃허브 커밋 이력 조회")
    public ResponseEntity<ApiResponse<List<String>>> getTodayCommitUrls(@PathVariable String githubId) throws IOException {
        List<String> commitUrls = commitService.getTodayCommitUrlsForTest(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(commitUrls, CustomResponseStatus.SUCCESS));
    }


    @PostMapping("/todayCommit/{githubId}")
    @Operation(summary =  " AI피드백 요청 &&백엔드DB에 커밋 이력 생성", description = "user의 커밋 이력 생성 - 오늘 깃허브 커밋 이력이 있어야함")
    public ResponseEntity<ApiResponse<CommitHistory>> addReviewToCommit(@PathVariable String githubId) throws IOException {
        CommitHistory updatedCommitHistory = commitService.addReviewToCommit(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(updatedCommitHistory, CustomResponseStatus.SUCCESS));
    }

    @GetMapping("/commitHistory/{githubId}")
    @Operation(summary =  "역대 모든 커밋히스토리(백엔드DB에 있는거) 조회", description = "user의 모든 커밋 이력 조회")
    public ResponseEntity<ApiResponse<List<CommitHistory>>> getTodayCommits(@PathVariable String githubId) {
        List<CommitHistory> commitHistories = commitService.getCommitsByGithubId(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(commitHistories, CustomResponseStatus.SUCCESS));
    }


    @PostMapping("/good/{historyId}")
    @Operation(summary =  "해당 커밋의 좋아요버튼 숫자 1증가", description = "해당 historyId커밋의 좋아요 값 1 증가")
    public ResponseEntity<ApiResponse<Integer>> addGoodToCommit(@PathVariable long historyId) {
        int goodValue = commitService.addGoodToCommit(historyId).getGood();
        return ResponseEntity.ok().body(ApiResponse.createSuccess(goodValue, CustomResponseStatus.SUCCESS));
    }
    @PostMapping("/bad/{historyId}")
    @Operation(summary =  "해당 커밋의 싫어요버튼 숫자1 증가", description = "해당 historyId커밋의 싫어요 값 1 증가")
    public ResponseEntity<ApiResponse<Integer>> addBadToCommit(@PathVariable long historyId) {
        int badValue = commitService.addBadToCommit(historyId).getBad();
        return ResponseEntity.ok().body(ApiResponse.createSuccess(badValue, CustomResponseStatus.SUCCESS));
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
