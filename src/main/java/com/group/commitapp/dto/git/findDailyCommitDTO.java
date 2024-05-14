package com.group.commitapp.dto.git;

import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "당일 커밋 유무 DTO")
@Getter
public class findDailyCommitDTO {
    @Schema(description = "커밋 유무")
    private boolean isCommit;

}
