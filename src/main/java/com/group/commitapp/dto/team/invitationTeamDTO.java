package com.group.commitapp.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = " DTO")
@Getter
public class invitationTeamDTO {
    @Schema(description = "팀 아이디" , example ="1")
    private final long teamId;
    @Schema(description = "깃허브 아이디" , example ="githubId")
    private final String githubId;
    public invitationTeamDTO( int teamId, String githubId) {
        this.teamId = teamId;
        this.githubId = githubId;
    }


}
