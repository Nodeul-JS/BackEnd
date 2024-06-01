package com.group.commitapp.domain;

import com.group.commitapp.dto.request.team.CreateTeamRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    @Column(nullable = false, length = 255, name = "team_name")
    private String name;
    private Integer maxMember;
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    // 생성자에 DTO를 전달받아 초기화하는 생성자
    public Team(CreateTeamRequest request) {
        this.name = request.getGroupName();
        this.maxMember = request.getMaxMember();
        this.description = request.getDescription();
    }
}
