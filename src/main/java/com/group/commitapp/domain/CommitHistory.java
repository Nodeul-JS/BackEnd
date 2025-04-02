package com.group.commitapp.domain;

import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;
import java.util.Date;

@Entity
@Table(name = "commitHistory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String description;
    private Integer good;
    private Integer bad;
    private String githubLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Builder
    private CommitHistory(String title, String description, String githubLink, User user) {
        this.title = title;
        this.description = description;
        this.githubLink = githubLink;
        this.user = user;
        this.good = 0;
        this.bad = 0;
    }

    public static CommitHistory create(String title, String description, String githubLink, User user) {
        return CommitHistory.builder()
                .title(title)
                .description(description)
                .githubLink(githubLink)
                .user(user)
                .build();
    }
    public void addGood() {
        this.good++;
    }
    public void addBad() {
        this.bad++;
    }
}
