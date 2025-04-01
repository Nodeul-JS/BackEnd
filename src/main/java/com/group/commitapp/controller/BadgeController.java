package com.group.commitapp.controller;


import com.group.commitapp.common.dto.ApiResponse;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.dto.badge.findBadgeDTO;
import com.group.commitapp.dto.badge.findBadgeListDTO;
import com.group.commitapp.service.BadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "뱃지", description = "뱃지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/badge")
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/myBadgeList/{githubId}")
    @Operation(summary =  "My 뱃지 전체 조회 ", description = "사용자가 가진 뱃지 정보 반환")
    public ResponseEntity<ApiResponse<List<findBadgeListDTO>>> findBadgeList(@PathVariable String githubId){
        List<findBadgeListDTO> DTOList = badgeService.findBadgeList(githubId);
        return ResponseEntity.ok(ApiResponse.createSuccess(DTOList, CustomResponseStatus.SUCCESS));
    }


    @Operation(summary =  "뱃지 상세 조회 ", description = "뱃지 아이디로 뱃지 정보 반환")
    @GetMapping("/{badgeId}")
    public ResponseEntity<ApiResponse<findBadgeDTO>> findBadge(@PathVariable Long badgeId ){
        findBadgeDTO badgeInfo = badgeService.findBadge(badgeId);
        return ResponseEntity.ok(ApiResponse.createSuccess(badgeInfo, CustomResponseStatus.SUCCESS));
    }











}
