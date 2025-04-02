package com.group.commitapp.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Schema(description = "Group Create DTO")
public class TeamCreateRequest {
    @Schema(description = "팀 이름")
    private String teamName;

    @Schema(description = "팀 설명")
    private String description;

    @Schema(description = "팀리더(팀 개설자) githubID")
    private String githubId;
    @Schema(description = "팀 최대 인원 수")
    private  int maxMember;


}
