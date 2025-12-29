package com.pinback.application.google.dto.response;

import org.springframework.lang.Nullable;

public record GoogleApiResponse(
	String id,
	String email,
	Boolean verifiedEmail,
	String name,
	@Nullable String givenName,
	@Nullable String familyName,
	String picture
) {
}
