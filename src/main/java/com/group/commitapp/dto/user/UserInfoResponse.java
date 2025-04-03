package com.group.commitapp.dto.user;

import com.group.commitapp.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoResponse(
        @Schema(description = "유저의 githubID")
        String githubId,
        @Schema(description = "유저 레벨")
        Integer level,
        @Schema(description = "유저 경험치")
        Integer experience
) {
    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .githubId(user.getGithubId())
                .level(user.getLevel())
                .experience(user.getExperience())
                .build();
    }
    public static List<UserInfoResponse> fromList(List<User> users) {
        return users.stream()
                .map(UserInfoResponse::from)
                .toList();
    }
}
