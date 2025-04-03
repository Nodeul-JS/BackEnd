package com.group.commitapp.dto.team;

import com.group.commitapp.domain.Team;
import lombok.*;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Builder
public record TeamInfoResponse(
        @Schema(description = "팀 아이디" , example ="1")
        long teamId,
        @Schema(description = "팀 이름" , example ="teamName")
        String teamName,
        @Schema(description = "팀 현재 인원수" , example ="4")
        int currentMembers,
        @Schema(description = "팀 최대 인원수" , example ="10")
        int  maxMembers,
        @Schema(description = "팀 설명" , example ="teamDescription")
        String Description
) {
    public static TeamInfoResponse from(Team team) {
        return TeamInfoResponse.builder()
                .teamId(team.getId())
                .teamName(team.getTeamName())
                .currentMembers(team.getMembers().size())
                .maxMembers(team.getMaxMember())
                .Description(team.getDescription())
                .build();
    }
    public static List<TeamInfoResponse> fromList(List<Team> teams) {
        return teams.stream()
                .map(TeamInfoResponse::from)
                .toList();
    }
}