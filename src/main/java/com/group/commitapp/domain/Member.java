package com.group.commitapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isHead;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;


    @Builder
    private Member(User user, Team team, boolean isHead){
        this.user = user;
        this.team = team;
        this.isHead = isHead;
    }

    public static Member createAsMember(User user, Team team){
        return Member.builder()
                .user(user)
                .team(team)
                .isHead(false)
                .build();
    }

    public static Member createAsLeader(User user, Team team){
        return Member.builder()
                .user(user)
                .team(team)
                .isHead(true)
                .build();
    }

    public Team geTeam(){
        return this.team;
    }
}
