package com.pinback.application.auth.dto;

public record SignUpResponse(
	String token
) {
	public static SignUpResponse from(String token) {
		return new SignUpResponse(token);
	}
}
