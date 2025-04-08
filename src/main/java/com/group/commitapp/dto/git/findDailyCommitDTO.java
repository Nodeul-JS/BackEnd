package com.group.commitapp.dto.git;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "당일 커밋 유무 DTO")
@Getter
public class findDailyCommitDTO {
	@Schema(description = "커밋 유무")
	private boolean isCommit;
}
