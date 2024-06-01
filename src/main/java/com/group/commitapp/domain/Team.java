package com.group.commitapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    @Column(nullable = false, length = 255, name = "team_name")
    private String TeamName;
    private Integer maxMember;
    private String description;
    private boolean isDeleted;



    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> members;

    // 생성자에 DTO를 전달받아 초기화하는 생성자
    public Team(CreateTeamRequest request) {
        this.name = request.getGroupName();
        this.maxMember = request.getMaxMember();
        this.description = request.getDescription();
    }
}
