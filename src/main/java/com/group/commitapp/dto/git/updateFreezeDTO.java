package com.group.commitapp.dto.git;

import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "update freeze number DTO")
@Getter
public class updateFreezeDTO {
@Schema(description = "프리즈 갯수", example = "20")
private Integer freezeCnt;

}
