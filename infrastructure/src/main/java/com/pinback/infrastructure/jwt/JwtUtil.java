package com.pinback.infrastructure.jwt;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pinback.infrastructure.jwt.exception.ExpiredTokenException;
import com.pinback.infrastructure.jwt.exception.InvalidTokenException;

@Component
public class JwtUtil {
	private static final String ID_CLAIM = "id";

	private final String key;
	private final long accessExpirationPeriod;
	private final JWTVerifier jwtVerifier;

	public JwtUtil(@Value("${jwt.secret-key}") String key,
		@Value("${jwt.accessExpirationPeriod}") long accessExpirationPeriod, @Value("${jwt.issuer}") String issuer) {
		this.key = key;
		this.accessExpirationPeriod = accessExpirationPeriod;
		this.jwtVerifier = JWT.require(Algorithm.HMAC512(key))
			.withClaimPresence(ID_CLAIM)
			.withIssuer(issuer)
			.build();
	}

	public String extractToken(String token) {
		return token.substring(7);
	}

	public UUID extractId(String accessToken) {
		return UUID.fromString(validateToken(accessToken)
			.getClaim(ID_CLAIM)
			.asString());
	}

	private DecodedJWT validateToken(String token) {
		try {
			return jwtVerifier.verify(token);
		} catch (TokenExpiredException e) {
			throw new ExpiredTokenException();
		} catch (JWTVerificationException e) {
			throw new InvalidTokenException();
		}
	}
}
