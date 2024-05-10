package com.group.commitapp.group.domain;

import com.group.commitapp.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Table(name = "member")
public class Member {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private boolean isHead;

    public Member(User user, Team team) {

        this.user = user;
        this.team = team;
    }

    public Member() {

    }
}
