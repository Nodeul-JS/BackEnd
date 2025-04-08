package com.group.commitapp.dto.request.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamRequest {
	private String groupName;
	private Integer maxMember;
	private String description;
	private Long leaderId;
}
