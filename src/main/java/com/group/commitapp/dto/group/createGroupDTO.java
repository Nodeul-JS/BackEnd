package com.group.commitapp.dto.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@Schema(description = "Group Create DTO")
public class createGroupDTO {
    @Schema(description = "그룹 이름")
    private String groupName;
    @Schema(description = "그룹 설명")
    private String groupDescription;
    @Schema(description = "그룹 인원 수")
    private int groupMemberNum;
    @Schema(description = "그룹 설명")
    private String description;



}
