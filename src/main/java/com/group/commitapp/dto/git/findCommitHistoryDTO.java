package com.group.commitapp.dto.git;

import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "GET: 해당 날짜의 깃 링크 연결, 화면에 출력 : githubLink")
@Getter
public class findCommitHistoryDTO {
    @Schema(description = "github 링크")
    private String githubLink;
}
