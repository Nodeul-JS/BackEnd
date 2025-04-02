package com.group.commitapp.dto.team;

import com.group.commitapp.domain.Member;

import java.time.LocalDateTime;

public record MemberInviteResponse(
        Long userId,
        String githubId,
        Long teamId,
        String teamName,
        LocalDateTime invitedAt,
        String message
) {
    public static MemberInviteResponse from(Member member) {
        return new MemberInviteResponse(
                member.getUser().getId(),
                member.getUser().getGithubId(),
                member.getTeam().getId(),
                member.getTeam().getTeamName(),
                member.getCreatedAt(),
                "초대가 완료되었습니다."
        );
    }
}