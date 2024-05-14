package com.group.commitapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;
  private String githubId;
  private Integer level;
  private Integer freezeCnt;
  private Integer experience;
  private String name;

  private String providerId;

  @Enumerated(value = EnumType.STRING)
  private Role role; // OAuth

  private User(String githubId, String providerId) { // github login
//    this.name = name;
    this.githubId = githubId; // 깃허브 아이디
    this.providerId = providerId; //식별 고유번호
//    this.role = Role.ROLE_USER; //어드민or유저계정인지 체크
  }

  public static User of(String githubId, String providerId) {
    if (githubId == null || githubId.isBlank()) {
      throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", githubId));
    }
    return new User(githubId, providerId);
  }


  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Member> members;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<CommitHistory> commitHistories;

  @JsonIgnore
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<BadgeHistory> badgeHistories;


  public void updateName(String name) {
    this.name = name;
  }


}
