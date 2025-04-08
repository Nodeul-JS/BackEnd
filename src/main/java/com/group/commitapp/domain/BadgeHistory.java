package com.group.commitapp.domain;

import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badgeHistory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BadgeHistory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "badgeId", nullable = false)
	private Badge badge;

	@Builder
	private BadgeHistory(User user, Badge badge) {
		this.user = user;
		this.badge = badge;
	}
	// Static factory method to create a new BadgeHistory
	public static BadgeHistory create(User user, Badge badge) {
		return BadgeHistory.builder().user(user).badge(badge).build();
	}
}
