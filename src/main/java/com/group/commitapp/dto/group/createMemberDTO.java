package com.group.commitapp.dto.group;

import lombok.Getter;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = " DTO")
@Getter
@AllArgsConstructor
public class createMemberDTO {
    @Schema(description = "유저 아이디")
    private Long userId;
    @Schema(description = "팀 아이디")
    private Long teamId;
}
