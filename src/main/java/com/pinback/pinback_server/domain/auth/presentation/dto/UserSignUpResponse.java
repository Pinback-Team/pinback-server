package com.pinback.pinback_server.domain.auth.presentation.dto;

public record UserSignUpResponse(
	String token
) {
	public static UserSignUpResponse from(String token) {
		return new UserSignUpResponse(token);
	}
}
