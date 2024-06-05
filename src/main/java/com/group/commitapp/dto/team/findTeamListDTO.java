package com.group.commitapp.dto.team;

import com.group.commitapp.domain.Team;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = " DTO")
@Getter
@RequiredArgsConstructor
public class findTeamListDTO {

    public findTeamListDTO(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.currentMembers = team.getMembers().size();
        this.maxMembers = team.getMaxMember();
        this.Description = team.getDescription();
    }

    @Schema(description = "팀 아이디" , example ="1")
    private long teamId;
    @Schema(description = "팀 이름" , example ="teamName")
    private String teamName;
    @Schema(description = "팀 현재 인원수" , example ="4")
    private int currentMembers;
    @Schema(description = "팀 최대 인원수" , example ="10")
    private int  maxMembers;
    @Schema(description = "팀 설명" , example ="teamDescription")
    private String Description;
}
