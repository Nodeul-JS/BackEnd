package com.group.commitapp.dto.oauth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessToken(String accessToken, String tokenType) {
	@JsonCreator
	public AccessToken(
			@JsonProperty("access_token") String accessToken,
			@JsonProperty("token_type") String tokenType) {

		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
}
