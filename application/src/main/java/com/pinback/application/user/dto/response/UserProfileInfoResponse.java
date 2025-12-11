package com.pinback.application.user.dto.response;

public record UserProfileInfoResponse(
	String name,
	String email,
	String remindAt,
	String profileImage
) {
	public static UserProfileInfoResponse of(String name, String email, String remindAt, String profileImage) {
		return new UserProfileInfoResponse(name, email, remindAt, profileImage);
	}
}
