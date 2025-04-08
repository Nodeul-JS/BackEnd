package com.group.commitapp.dto.badge;

import com.group.commitapp.domain.Badge;
import com.group.commitapp.domain.BadgeHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Schema(description = "획득한 뱃지 리스트 출력 DTO -> [badgeName,\n" + "descrip,\n" + "createdAt]")
@Builder
public record findBadgeListResponse(
		@Schema(description = "뱃지 ID") Long badgeId,
		@Schema(description = "뱃지 이름") String badgeName,
		@Schema(description = "뱃지 설명") String description,
		@Schema(description = "뱃지 획득 날짜") LocalDateTime createdAt) {
	public static findBadgeListResponse from(BadgeHistory badgeHistory) {
		Badge badge = badgeHistory.getBadge();
		return findBadgeListResponse
				.builder()
				.badgeId(badge.getId())
				.badgeName(badge.getName())
				.description(badge.getDescription())
				.createdAt(badgeHistory.getCreatedAt())
				.build();
	}

	public static List<findBadgeListResponse> fromList(List<BadgeHistory> badgeHistories) {
		return badgeHistories.stream().map(findBadgeListResponse::from).toList();
	}
}
