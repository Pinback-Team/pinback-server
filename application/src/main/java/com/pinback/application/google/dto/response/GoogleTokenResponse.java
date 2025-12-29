package com.pinback.application.google.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleTokenResponse(
	String accessToken,
	String expiresIn,
	String tokenType,
	String scope,
	String idToken
) {
}
