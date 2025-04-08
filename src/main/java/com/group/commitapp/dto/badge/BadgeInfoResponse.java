package com.group.commitapp.dto.badge;

import com.group.commitapp.domain.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "GET:단일 뱃지 조회 DTO")
@Builder
public record BadgeInfoResponse(
		@Schema(description = "뱃지 이름", example = "Best Commiter") String badgeName,
		@Schema(description = "뱃지 설명") String description) {
	public static BadgeInfoResponse from(Badge badge) {
		return BadgeInfoResponse.builder()
				.badgeName(badge.getName())
				.description(badge.getDescription())
				.build();
	}
}
