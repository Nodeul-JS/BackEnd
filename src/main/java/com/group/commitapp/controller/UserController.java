package com.group.commitapp.controller;

import com.group.commitapp.common.dto.ApiResponse;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.dto.UserInfoDTO;
import com.group.commitapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "유저", description = "User 관련 API")
public class UserController {

    private final UserService userService;


    @Operation(summary =  "user정보 조회", description = "githubId로 user정보 조회")
    @GetMapping("/mypage/{githubId}")
    public ResponseEntity<ApiResponse<UserInfoDTO>> getUserByGithubId(@PathVariable String githubId) {
        UserInfoDTO userInfoDTO = new UserInfoDTO(userService.getUserByGithubId(githubId));
        return ResponseEntity.ok().body(ApiResponse.createSuccess(userInfoDTO, CustomResponseStatus.SUCCESS));
    }



}
