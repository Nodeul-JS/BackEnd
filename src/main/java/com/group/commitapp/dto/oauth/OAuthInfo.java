package com.group.commitapp.dto.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class OAuthInfo {
	private final String username;
	private final String idNumber;

	@JsonCreator
	public OAuthInfo(@JsonProperty("login") String username, @JsonProperty("id") String idNumber) {

		this.username = username;
		this.idNumber = idNumber;
	}
}
