package com.group.commitapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private Boolean isHead;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

    public Member(User user, Team team){
        this(user, team, false);
    }
    public Member(User user, Team team, boolean isHead){
        this.user = user;
        this.team = team;
        this.isHead = isHead;
    }
//    public static Member saveMember(User user, Team team, boolean isHead){
//        Member member = new Member();
//        member.setUser(user);
//        member.setTeam(team);
//        member.setIsHead(isHead);
//        return member;
//    }


}
