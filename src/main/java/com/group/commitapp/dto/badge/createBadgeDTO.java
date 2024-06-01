package com.group.commitapp.dto.badge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "POST: 뱃지 생성 DTO")
@Getter
public class createBadgeDTO {

    @Schema(description = "뱃지 이름", example = "Best Commiter")
    private String badgeName;
    @Schema(description = "뱃지 설명", example = "커밋을 가장 많이 한 사람에게 주어지는 뱃지")
    private String badgeDescription;
    @Schema(description = "뱃지 생성 날짜", example = "2021-09-01")
    private Data createdAt;
}
