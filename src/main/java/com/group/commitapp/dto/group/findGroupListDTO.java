package com.group.commitapp.dto.group;

import com.group.commitapp.domain.Team;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = " DTO")
@Getter
@RequiredArgsConstructor
public class findGroupListDTO {

    public findGroupListDTO(Team team) {
        this.teamId = team.getTeamId();
        this.teamName = team.getTeamName();
        this.teamSize = team.getMembers().size();
        this.Description = team.getDescription();

    }
@Schema(description = "팀 아이디" , example ="1")
private long teamId;
@Schema(description = "팀 이름" , example ="teamName")
private String teamName;
@Schema(description = "팀 인원수" , example ="4")
private int teamSize;
@Schema(description = "팀 설명" , example ="teamDescription")
private String Description;


}
