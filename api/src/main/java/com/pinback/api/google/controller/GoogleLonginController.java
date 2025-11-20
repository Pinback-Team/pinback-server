package com.pinback.api.google.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinback.api.google.dto.request.GoogleLoginRequest;
import com.pinback.application.auth.usecase.AuthUsecase;
import com.pinback.application.google.dto.response.GoogleLoginResponse;
import com.pinback.application.google.usecase.GoogleUsecase;
import com.pinback.shared.dto.ResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v2/auth/google")
@RequiredArgsConstructor
@Tag(name = "Google", description = "구글 소셜 로그인 API")
public class GoogleLonginController {

	private final GoogleUsecase googleUsecase;
	private final AuthUsecase authUsecase;

	@Operation(summary = "구글 소셜 로그인", description = "구글을 통한 소셜 로그인을 진행합니다")
	@PostMapping()
	public Mono<ResponseDto<GoogleLoginResponse>> googleLogin(
		@Valid @RequestBody GoogleLoginRequest request
	) {
		return googleUsecase.getUserInfo(request.toCommand())
			.flatMap(googleResponse -> {
				return authUsecase.getInfoAndToken(googleResponse.email())
					.map(loginResponse -> {
						return ResponseDto.ok(loginResponse);
					});
			});
	}
}
