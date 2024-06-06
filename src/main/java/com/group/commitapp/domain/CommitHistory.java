package com.group.commitapp.domain;

import com.group.commitapp.dto.commit.CommitHistoryDTO;
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
    @Lob
    private String description;
//    private String askGpt;
//    private Boolean isCommit;
//    private Boolean isDescript;
    private Date createdAt;
    private Integer good;
    private Integer bad;
    private String githubLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public CommitHistory(CommitHistoryDTO commitHistoryDTO, User user) {
        this.user = user;
        this.title = commitHistoryDTO.getTitle();
        this.description = commitHistoryDTO.getDescription();
        this.githubLink = commitHistoryDTO.getGithubLink();
        this.createdAt = new Date();
        this.good = 0;
        this.bad = 0;
    }

}
