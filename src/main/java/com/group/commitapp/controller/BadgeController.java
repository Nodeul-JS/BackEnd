package com.group.commitapp.controller;


import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.dto.badge.createBadgeDTO;
import com.group.commitapp.dto.badge.findBadgeDTO;
import com.group.commitapp.dto.badge.findBadgeListDTO;
import com.group.commitapp.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "뱃지", description = "뱃지 관련 API")
@RestController
@RequestMapping(value = "/api/badge")
public class BadgeController {
    private final BadgeService badgeService;


    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;

    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    ///여기서 부터 api 작성
    @GetMapping("/{githubId}")
    @Operation(summary =  "My 뱃지 전체 조회 ", description = "사용자가 가진 뱃지 정보 반환",
            responses = {
                    @ApiResponse(description = "조회된 뱃지 정보를 반환함.")
            }
    )
    public Result<List<findBadgeListDTO>> findBadgeList(@PathVariable Long usersId){
        List<findBadgeListDTO> DTOList = badgeService.findBadgeList(usersId);
        return new Result<>(DTOList);
    }


    @Operation(summary =  "뱃지 단일 조회 ", description = "뱃지 아이디로 뱃지 정보 반환",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회된 뱃지 정보를 반환함."
                    )
            }
    )
    @GetMapping("/{badgeId}")
    public Result<findBadgeDTO> findBadge(@PathVariable Long badgeId ){
        findBadgeDTO badgeInfo = badgeService.findBadge(badgeId);
        return new Result<>(badgeInfo);
    }

    @PostMapping("/create/{usersId}/{badgeId}")
    @Operation(summary =  "뱃지 지급 ", description = "사용자에게 뱃지 지급",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "뱃지 지급 성공"
                    )
            }
    )
    public void createBadge(@PathVariable Long usersId, @PathVariable Long badgeId){
         badgeService.createBadge(usersId, badgeId);
    }








}
