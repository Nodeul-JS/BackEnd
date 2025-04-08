package com.group.commitapp.dto.team;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = " DTO")
@Getter
@AllArgsConstructor
public class MemberCreateRequest {
	@Schema(description = "유저 아이디")
	private Long userId;

	@Schema(description = "팀 아이디")
	private Long teamId;
}
