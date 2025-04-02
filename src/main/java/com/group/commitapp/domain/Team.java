package com.group.commitapp.domain;

import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255, name = "team_name")
    private String teamName;

    @Column(nullable = false)
    private Integer maxMember;

    @Column(length = 1000)
    private String description;

    private boolean isDeleted = false;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> members = new ArrayList<>();

    @Builder
    private Team(String teamName, Integer maxMember, String description) {
        this.teamName = teamName;
        this.maxMember = maxMember;
        this.description = description;
    }

    public static Team createWithLeader(String teamName, Integer maxMember, String description, User leader) {
        Team team = Team.builder()
                .teamName(teamName)
                .maxMember(maxMember)
                .description(description)
                .build();

        team.addLeader(leader);
        return team;
    }

    public void addLeader(User user){
        Member leader = Member.createAsLeader(user, this);
        this.members.add(leader);
    }
    public void addMember(User user){
        Member member = Member.createAsMember(user, this);
        this.members.add(member);
    }

    public void deleteTeam(){
        this.isDeleted = true;
    }

}
