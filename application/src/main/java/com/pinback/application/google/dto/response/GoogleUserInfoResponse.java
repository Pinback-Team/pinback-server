package com.pinback.application.google.dto.response;

public record GoogleUserInfoResponse(
	String email,
	String pictureUrl,
	String name
) {
	public static GoogleUserInfoResponse from(String email, String pictureUrl, String name) {
		return new GoogleUserInfoResponse(email, pictureUrl, name);
	}
}
