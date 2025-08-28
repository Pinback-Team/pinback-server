package com.pinback.application.auth.service;

import java.util.UUID;

public interface JwtProvider {
	String createAccessToken(UUID userId);

	boolean validateToken(String token);

	UUID getUserIdFromToken(String token);
}
