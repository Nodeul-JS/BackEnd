package com.group.commitapp.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "badgeHistory")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rewardId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // 생성 시간 설정
    }

    // Static factory method to create a new BadgeHistory
    public static BadgeHistory saveBadgeHistory(User user, Badge badge) {
        BadgeHistory badgeHistory = new BadgeHistory();
        badgeHistory.setUser(user);
        badgeHistory.setBadge(badge);
        return badgeHistory;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badgeId", nullable = false)
    private Badge badge;


}