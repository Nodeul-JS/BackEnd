package com.group.commitapp.dto.commit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommitHistoryDTO {
    @Schema(description = "커밋 제목")
    private String title;
    @Schema(description = "커밋 설명")
    private String description;
    @Schema(description = "깃허브 링크")
    private String githubLink;
//    @Schema(description = "유저  아이디")
//    private Long userId; // 유저 ID를 받습니다.
    @Schema(description = "깃허브 아이디")
    private String githubId;
}
