package com.group.commitapp.dto.commit;

import com.group.commitapp.domain.CommitHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Schema(description = "커밋 히스토리 응답 DTO")
public record CommitHistoryResponse (
    @Schema(description = "커밋 제목")
    String title,
    @Schema(description = "커밋히스토리 ID")
    Long id,
    @Schema(description = "커밋 설명")
    String description,
    @Schema(description = "깃허브 링크")
    String githubLink,
    @Schema(description = "깃허브 아이디")
    String githubId,
    @Schema(description = "커밋 날짜")
    LocalDateTime createdAt
) {
    public static CommitHistoryResponse from(CommitHistory commitHistory) {
        return CommitHistoryResponse.builder()
                .title(commitHistory.getTitle())
                .id(commitHistory.getId())
                .description(commitHistory.getDescription())
                .githubLink(commitHistory.getGithubLink())
                .githubId(commitHistory.getUser().getGithubId())
                .createdAt(commitHistory.getCreatedAt())
                .build();
    }
    public static List<CommitHistoryResponse> fromList(List<CommitHistory> commitHistories) {
        return commitHistories.stream()
                .map(CommitHistoryResponse::from)
                .toList();
    }
}