package com.pinback.pinback_server.global.common.jwt;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtProvider {
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String ID_CLAIM = "id";

	private final String key;
	private final long accessExpirationPeriod;
	private final JWTCreator.Builder jwtBuilder;

	public JwtProvider(@Value("${jwt.secret-key}") String key,
		@Value("${jwt.accessExpirationPeriod}") long accessExpirationPeriod, @Value("${jwt.issuer}") String issuer) {

		this.key = key;
		this.accessExpirationPeriod = accessExpirationPeriod;
		this.jwtBuilder = JWT.create()
			.withIssuer(issuer);
	}

	public String createAccessToken(UUID id) {
		return jwtBuilder
			.withClaim(ID_CLAIM, id.toString())
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(Instant.now().plusMillis(accessExpirationPeriod))
			.sign(Algorithm.HMAC512(key));
	}
}
