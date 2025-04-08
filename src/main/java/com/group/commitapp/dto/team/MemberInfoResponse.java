package com.group.commitapp.dto.team;

import com.group.commitapp.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record MemberInfoResponse(
		@Schema(description = "그룹원 이름: User 테이블") String githubId,
		@Schema(description = "그룹원의 커밋 여부: User 테이블") boolean isCommit,
		@Schema(description = "그룹원의 레벨 : User 테이블") int level,
		@Schema(description = "그룹원의 경험치 : User 테이블") int exp
		//        @Schema(description = "그룹 최대 인원 수 : Team 테이블")
		//        int maxMember,
		//        @Schema(description = "그룹 현재 인원 수: Team 테이블에 연결된 Member 테이블의 인원 수")
		//        int currentMember
		) {
	public static MemberInfoResponse from(Member member) {
		return MemberInfoResponse.builder()
				.githubId(member.getUser().getGithubId())
				.isCommit(member.getUser().isCommit())
				.level(member.getUser().getLevel())
				.exp(member.getUser().getExperience())
				.build();
	}

	public static List<MemberInfoResponse> fromList(List<Member> members) {
		return members.stream().map(MemberInfoResponse::from).toList();
	}
}
