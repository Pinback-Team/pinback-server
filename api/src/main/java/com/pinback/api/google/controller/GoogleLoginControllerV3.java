package com.pinback.api.google.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.auth.dto.request.SignUpRequestV3;
import com.pinback.api.google.dto.request.GoogleLoginRequest;
import com.pinback.application.auth.dto.SignUpResponse;
import com.pinback.application.auth.usecase.AuthUsecase;
import com.pinback.application.google.dto.response.GoogleLoginResponseV3;
import com.pinback.application.google.usecase.GoogleUsecase;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/auth")
@RequiredArgsConstructor
@Tag(name = "Google OAuth V3", description = "구글 소셜 로그인 API V3")
public class GoogleLoginControllerV3 {
	private final GoogleUsecase googleUsecase;
	private final AuthUsecase authUsecase;

	@Operation(summary = "구글 소셜 로그인 V3", description = "구글 소셜 로그인을 진행하며, 응답에 직무 선택 여부를 포함합니다.")
	@PostMapping("/google")
	public Mono<ResponseDto<GoogleLoginResponseV3>> googleLogin(
		@Valid @RequestBody GoogleLoginRequest request
	) {
		return googleUsecase.getUserInfo(request.toCommand())
			.flatMap(googleResponse -> {
				return authUsecase.getInfoAndTokenV3(googleResponse.email(), googleResponse.pictureUrl(),
						googleResponse.name())
					.map(loginResponse -> {
						return ResponseDto.ok(loginResponse);
					});
			});
	}

	@Operation(summary = "신규 회원 온보딩 V3", description = "신규 회원의 기본 정보(직무 포함)를 등록합니다.")
	@PatchMapping("/signup")
	public ResponseDto<SignUpResponse> signUpV3(
		@Valid @RequestBody SignUpRequestV3 request
	) {
		SignUpResponse response = authUsecase.signUpV3(request.toCommand());
		return ResponseDto.ok(response);
	}
}
