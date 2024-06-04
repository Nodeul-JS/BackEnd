package com.group.commitapp.dto.commit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommitReviewDTO {
//    private Long commitHistoryId;
    @Schema(description = "깃허브 아이디")
    private String githubId;

}
