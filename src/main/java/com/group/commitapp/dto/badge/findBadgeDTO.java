package com.group.commitapp.dto.badge;

import com.group.commitapp.domain.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "GET:단일 뱃지 조회 DTO")
@Getter
public class findBadgeDTO {
    public findBadgeDTO(Badge badge) {
        this.badgeName = badge.getName();
        this.Description = badge.getDescription();
    }
    @Schema(description = "뱃지 이름", example = "Best Commiter")
    private String badgeName;
    @Schema(description = "뱃지 설명")
    private String Description;

}
