package com.pinback.infrastructure.jwt;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.pinback.application.auth.service.JwtProvider;

@Component
public class JwtProviderImpl implements JwtProvider {
	private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
	private static final String ID_CLAIM = "id";

	private final String key;
	private final long accessExpirationPeriod;
	private final JWTCreator.Builder jwtBuilder;

	public JwtProviderImpl(@Value("${jwt.secret-key}") String key,
		@Value("${jwt.accessExpirationPeriod}") long accessExpirationPeriod, @Value("${jwt.issuer}") String issuer) {

		this.key = key;
		this.accessExpirationPeriod = accessExpirationPeriod;
		this.jwtBuilder = JWT.create()
			.withIssuer(issuer);
	}

	@Override
	public String createAccessToken(UUID id) {
		return jwtBuilder
			.withClaim(ID_CLAIM, id.toString())
			.withSubject(ACCESS_TOKEN_SUBJECT)
			.withExpiresAt(Instant.now().plusMillis(accessExpirationPeriod))
			.sign(Algorithm.HMAC512(key));
	}

	@Override
	public boolean validateToken(String token) {
		try {
			getVerifier().verify(token);
			return true;
		} catch (JWTVerificationException e) {
			return false;
		}
	}

	@Override
	public UUID getUserIdFromToken(String token) {
		DecodedJWT decodedJWT = getVerifier().verify(token);
		String userIdStr = decodedJWT.getClaim(ID_CLAIM).asString();
		return UUID.fromString(userIdStr);
	}

	private JWTVerifier getVerifier() {
		return JWT.require(Algorithm.HMAC512(key)).build();
	}
}
