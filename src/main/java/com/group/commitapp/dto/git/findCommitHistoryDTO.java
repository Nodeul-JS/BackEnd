package com.group.commitapp.dto.git;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Schema(description = "GET: 해당 날짜의 깃 링크 연결, 화면에 출력 : githubLink")
@Getter
public class findCommitHistoryDTO {
	@Schema(description = "github 링크")
	private String githubLink;
}
