package com.group.commitapp.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "commitHistory")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    private String title;
    private String description;
    private String askGpt;
    private Boolean isCommit;
    private Boolean isDescript;
    private Date createdAt;
    private Integer good;
    private Integer bad;
    private String githubLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;


}
