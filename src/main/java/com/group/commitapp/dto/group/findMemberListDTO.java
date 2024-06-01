package com.group.commitapp.dto.group;


import com.group.commitapp.domain.Member;
import lombok.Getter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = " DTO")
@Getter
public class findMemberListDTO {
    public findMemberListDTO(Member member) {
        this.UserName = member.getUsers().getName();
        this.isCommit = member.getUsers().isCommit();
        this.level = member.getUsers().getLevel();
        this.maxMember = member.getTeam().getMaxMember();
        this.currentMember = member.getTeam().getMembers().size();
    }

    @Schema(description = "그룹원 이름: User 테이블")
   private String UserName;
    @Schema(description = "그룹원의 커밋 여부: User 테이블")
    private boolean isCommit;
    @Schema(description = "그룹원의 레벨 : User 테이블")
    private int level;
    @Schema(description = "그룹 최대 인원 수 : Team 테이블")
    private int maxMember;
    @Schema(description = "그룹 현재 인원 수: Team 테이블에 연결된 Member 테이블의 인원 수")
    private int currentMember;

}
