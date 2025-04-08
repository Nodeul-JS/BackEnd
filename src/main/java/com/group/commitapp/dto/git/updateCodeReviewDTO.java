package com.group.commitapp.dto.git;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "code review number update DTO")
@Getter
public class updateCodeReviewDTO {
	@Schema(description = "code review number", example = "1")
	private Integer good;

	@Schema(description = "code review number", example = "1")
	private Integer bad;
}
