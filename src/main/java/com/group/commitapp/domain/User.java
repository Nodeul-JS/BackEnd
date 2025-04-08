package com.group.commitapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group.commitapp.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String githubId;
	private Integer level;
	private Integer freezeCnt;
	private Integer experience;
	private String name;
	private boolean isCommit;
	private String providerId;

	@Enumerated(value = EnumType.STRING)
	private Role role; // OAuth

	@JsonIgnore
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private List<Member> members = new ArrayList<>();

	@JsonIgnore
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private List<CommitHistory> commitHistories = new ArrayList<>();

	;

	@JsonIgnore
	@OneToMany(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			orphanRemoval = true,
			fetch = FetchType.LAZY)
	private List<BadgeHistory> badgeHistories = new ArrayList<>();

	;

	@Builder
	private User(String githubId, String providerId) { // github login
		this.name = "";
		this.githubId = githubId; // 깃허브 아이디
		this.providerId = providerId; // 식별 고유번호
		this.role = Role.ROLE_USER; // 어드민or유저계정인지 체크
		this.level = 1; // 레벨
		this.experience = 0; // 경험치
		this.freezeCnt = 0; // 프리즈 카운트
		this.isCommit = false; // 커밋 여부
	}

	public static User create(String githubId, String providerId) {
		if (githubId == null || githubId.isBlank()) {
			throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", githubId));
		}

		return User.builder().githubId(githubId).providerId(providerId).build();
	}

	public void addExperience(int experience) {
		this.experience += experience;
		if (this.experience >= 100) {
			this.level += 1;
			this.experience = 0;
		}
	}

	public List<Team> getTeams() {
		return members.stream().map(Member::getTeam).toList();
	}
}
