package com.pinback.api.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.application.auth.dto.SignUpResponseV3;
import com.pinback.application.auth.dto.TokenResponse;
import com.pinback.application.auth.usecase.AuthUsecase;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v3/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication V3", description = "인증 관리 API V3")
public class AuthControllerV3 {
	private final AuthUsecase authUsecase;

	@Value("${jwt.refreshExpirationPeriod}")
	private long refreshTokenExpirationPeriod;

	@PostMapping("/reissue")
	public ResponseEntity<ResponseDto<TokenResponse>> reissueAccessToken(
		@CookieValue(name = "refreshToken") String refreshToken
	) {
		SignUpResponseV3 response = authUsecase.getNewToken(refreshToken);

		ResponseCookie cookie = ResponseCookie.from("refreshToken", response.refreshToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(refreshTokenExpirationPeriod / 1000)
			.sameSite("None")
			.build();

		return ResponseEntity.ok()
			.header(HttpHeaders.SET_COOKIE, cookie.toString())
			.body(ResponseDto.ok(TokenResponse.of(response.accessToken())));
	}

}
